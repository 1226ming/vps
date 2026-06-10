package com.example.vps.ticket.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.vps.common.entity.BaseEntity;
import com.example.vps.common.enums.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("vps_ticket_message")
@EqualsAndHashCode(callSuper = true)
public class VpsTicketMessage extends BaseEntity {
    private Long ticketId;
    private Long senderId;
    private UserRole senderRole;
    private String content;
}
