package com.example.vps.plan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.vps.common.entity.BaseEntity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("vps_plan")
@EqualsAndHashCode(callSuper = true)
public class VpsPlan extends BaseEntity {
    private String name;
    private Integer cpuCore;
    private Integer memoryMb;
    private Integer diskGb;
    private Integer bandwidthMbps;
    private Integer trafficGb;
    private BigDecimal price;
    private String description;
    private Boolean enabled;
}
