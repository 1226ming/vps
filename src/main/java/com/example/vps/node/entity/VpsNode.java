package com.example.vps.node.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.vps.common.entity.BaseEntity;
import com.example.vps.common.enums.NodeStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("vps_node")
@EqualsAndHashCode(callSuper = true)
public class VpsNode extends BaseEntity {
    private String name;
    private String host;
    private Integer sshPort;
    private String sshUsername;
    private String sshPasswordEncrypted;
    private Integer cpuCore;
    private Integer memoryMb;
    private Integer diskGb;
    private NodeStatus status;
}
