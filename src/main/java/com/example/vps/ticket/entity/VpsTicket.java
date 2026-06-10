package com.example.vps.ticket.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.vps.common.entity.BaseEntity;
import com.example.vps.common.enums.TicketStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("vps_ticket")
@EqualsAndHashCode(callSuper = true)
public class VpsTicket extends BaseEntity {
    private Long userId;
    private String title;
    private String category;
    private TicketStatus status;
}
