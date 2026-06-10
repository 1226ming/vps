package com.example.vps.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.vps.common.entity.BaseEntity;
import com.example.vps.common.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("vps_order")
@EqualsAndHashCode(callSuper = true)
public class VpsOrder extends BaseEntity {
    private String orderNo;
    private Long userId;
    private Long planId;
    private Integer durationDays;
    private BigDecimal amount;
    private OrderStatus status;
    private LocalDateTime payTime;
}
