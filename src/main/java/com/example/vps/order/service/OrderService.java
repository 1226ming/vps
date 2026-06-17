package com.example.vps.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vps.common.enums.OrderStatus;
import com.example.vps.common.exception.BizException;
import com.example.vps.common.security.CurrentUser;
import com.example.vps.order.dto.CreateOrderRequest;
import com.example.vps.order.dto.UpdateOrderStatusRequest;
import com.example.vps.order.entity.VpsOrder;
import com.example.vps.order.mapper.VpsOrderMapper;
import com.example.vps.plan.entity.VpsPlan;
import com.example.vps.plan.mapper.VpsPlanMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final VpsOrderMapper orderMapper;
    private final VpsPlanMapper planMapper;

    public OrderService(VpsOrderMapper orderMapper, VpsPlanMapper planMapper) {
        this.orderMapper = orderMapper;
        this.planMapper = planMapper;
    }

    @Transactional
    public VpsOrder create(CreateOrderRequest request) {
        VpsPlan plan = planMapper.selectById(request.planId());
        if (plan == null) {
            throw new BizException(404, "套餐不存在");
        }
        VpsOrder order = new VpsOrder();
        order.setOrderNo("VPS" + UUID.randomUUID().toString().replace("-", "").substring(0, 18).toUpperCase());
        order.setUserId(CurrentUser.id());
        order.setPlanId(plan.getId());
        order.setDurationDays(request.durationDays());
        order.setAmount(plan.getPrice().multiply(java.math.BigDecimal.valueOf(request.durationDays()))
                .divide(java.math.BigDecimal.valueOf(30), 2, java.math.RoundingMode.HALF_UP));
        order.setStatus(OrderStatus.WAIT_PAY);
        orderMapper.insert(order);
        return order;
    }

    public IPage<VpsOrder> myOrders(long page, long size) {
        return orderMapper.selectPage(Page.of(page, size), new QueryWrapper<VpsOrder>()
                .eq("user_id", CurrentUser.id())
                .orderByDesc("id"));
    }

    public VpsOrder myDetail(Long id) {
        VpsOrder order = orderMapper.selectById(id);
        if (order == null || !order.getUserId().equals(CurrentUser.id())) {
            throw new BizException(404, "订单不存在");
        }
        return order;
    }

    public VpsOrder pay(Long id) {
        VpsOrder order = myDetail(id);
        if (order.getStatus() != OrderStatus.WAIT_PAY) {
            throw new BizException(409, "订单状态不允许支付");
        }
        order.setStatus(OrderStatus.PAID);
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return order;
    }

    public VpsOrder cancel(Long id) {
        VpsOrder order = myDetail(id);
        if (order.getStatus() != OrderStatus.WAIT_PAY) {
            throw new BizException(409, "订单状态不允许取消");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderMapper.updateById(order);
        return order;
    }

    public IPage<VpsOrder> adminOrders(long page, long size) {
        return orderMapper.selectPage(Page.of(page, size), null);
    }

    public VpsOrder updateStatus(Long id, UpdateOrderStatusRequest request) {
        VpsOrder order = orderMapper.selectById(id);
        if (order == null) {
            throw new BizException(404, "订单不存在");
        }
        order.setStatus(request.status());
        orderMapper.updateById(order);
        return order;
    }
}
