package com.example.vps.traffic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.vps.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("vps_traffic_snapshot")
@EqualsAndHashCode(callSuper = true)
public class VpsTrafficSnapshot extends BaseEntity {
    private Long instanceId;
    private Long lastInBytes;
    private Long lastOutBytes;
}
