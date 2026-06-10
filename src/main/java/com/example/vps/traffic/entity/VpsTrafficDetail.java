package com.example.vps.traffic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.vps.common.entity.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("vps_traffic_detail")
@EqualsAndHashCode(callSuper = true)
public class VpsTrafficDetail extends BaseEntity {
    private Long userId;
    private Long instanceId;
    private Long inBytes;
    private Long outBytes;
    private Long totalBytes;
    private LocalDateTime collectTime;
}
