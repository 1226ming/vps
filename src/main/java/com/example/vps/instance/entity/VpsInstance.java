package com.example.vps.instance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.vps.common.entity.BaseEntity;
import com.example.vps.common.enums.InstanceStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("vps_instance")
@EqualsAndHashCode(callSuper = true)
public class VpsInstance extends BaseEntity {
    private Long userId;
    private Long orderId;
    private Long planId;
    private Long nodeId;
    private String name;
    private String ipAddress;
    private String osName;
    private InstanceStatus status;
    private BigDecimal trafficUsedGb;
    private BigDecimal trafficLimitGb;
    private LocalDateTime expireTime;
}
