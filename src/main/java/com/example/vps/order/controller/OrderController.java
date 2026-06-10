package com.example.vps.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.vps.common.result.Result;
import com.example.vps.order.dto.CreateOrderRequest;
import com.example.vps.order.dto.UpdateOrderStatusRequest;
import com.example.vps.order.entity.VpsOrder;
import com.example.vps.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public Result<VpsOrder> create(@Valid @RequestBody CreateOrderRequest request) {
        return Result.success(orderService.create(request));
    }

    @GetMapping("/orders")
    public Result<IPage<VpsOrder>> myOrders(@RequestParam(defaultValue = "1") long page,
                                            @RequestParam(defaultValue = "20") long size) {
        return Result.success(orderService.myOrders(page, size));
    }

    @GetMapping("/orders/{id}")
    public Result<VpsOrder> detail(@PathVariable Long id) {
        return Result.success(orderService.myDetail(id));
    }

    @PostMapping("/orders/{id}/pay")
    public Result<VpsOrder> pay(@PathVariable Long id) {
        return Result.success(orderService.pay(id));
    }

    @PostMapping("/orders/{id}/cancel")
    public Result<VpsOrder> cancel(@PathVariable Long id) {
        return Result.success(orderService.cancel(id));
    }

    @GetMapping("/admin/orders")
    public Result<IPage<VpsOrder>> adminOrders(@RequestParam(defaultValue = "1") long page,
                                               @RequestParam(defaultValue = "20") long size) {
        return Result.success(orderService.adminOrders(page, size));
    }

    @PutMapping("/admin/orders/{id}/status")
    public Result<VpsOrder> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return Result.success(orderService.updateStatus(id, request));
    }
}
