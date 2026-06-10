package com.example.vps.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.vps.common.entity.BaseEntity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("vps_node_monitor")
@EqualsAndHashCode(callSuper = true)
public class VpsNodeMonitor extends BaseEntity {
    private Long nodeId;
    private BigDecimal cpuUsage;
    private Integer memoryUsedMb;
    private Integer memoryTotalMb;
    private Integer diskUsedGb;
    private Integer diskTotalGb;
    private BigDecimal load1;
    private BigDecimal load5;
    private BigDecimal load15;
    private Long inBytes;
    private Long outBytes;
}
