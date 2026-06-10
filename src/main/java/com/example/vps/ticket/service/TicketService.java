package com.example.vps.ticket.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vps.common.enums.TicketStatus;
import com.example.vps.common.enums.UserRole;
import com.example.vps.common.exception.BizException;
import com.example.vps.common.security.CurrentUser;
import com.example.vps.ticket.dto.CreateTicketRequest;
import com.example.vps.ticket.dto.TicketMessageRequest;
import com.example.vps.ticket.entity.VpsTicket;
import com.example.vps.ticket.entity.VpsTicketMessage;
import com.example.vps.ticket.mapper.VpsTicketMapper;
import com.example.vps.ticket.mapper.VpsTicketMessageMapper;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {
    private final VpsTicketMapper ticketMapper;
    private final VpsTicketMessageMapper messageMapper;

    public TicketService(VpsTicketMapper ticketMapper, VpsTicketMessageMapper messageMapper) {
        this.ticketMapper = ticketMapper;
        this.messageMapper = messageMapper;
    }

    @Transactional
    public VpsTicket create(CreateTicketRequest request) {
        VpsTicket ticket = new VpsTicket();
        ticket.setUserId(CurrentUser.id());
        ticket.setTitle(request.title());
        ticket.setCategory(request.category());
        ticket.setStatus(TicketStatus.OPEN);
        ticketMapper.insert(ticket);
        addMessage(ticket.getId(), CurrentUser.id(), UserRole.USER, request.content());
        return ticket;
    }

    public IPage<VpsTicket> myTickets(long page, long size) {
        return ticketMapper.selectPage(Page.of(page, size), new LambdaQueryWrapper<VpsTicket>()
                .eq(VpsTicket::getUserId, CurrentUser.id())
                .orderByDesc(VpsTicket::getId));
    }

    public Map<String, Object> detail(Long id) {
        VpsTicket ticket = requiredMyTicket(id);
        return Map.of("ticket", ticket, "messages", messages(id));
    }

    @Transactional
    public VpsTicket reply(Long id, TicketMessageRequest request) {
        VpsTicket ticket = requiredMyTicket(id);
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new BizException(409, "工单已关闭");
        }
        addMessage(id, CurrentUser.id(), UserRole.USER, request.content());
        ticket.setStatus(TicketStatus.WAIT_USER);
        ticketMapper.updateById(ticket);
        return ticket;
    }

    public VpsTicket close(Long id) {
        VpsTicket ticket = requiredMyTicket(id);
        ticket.setStatus(TicketStatus.CLOSED);
        ticketMapper.updateById(ticket);
        return ticket;
    }

    public IPage<VpsTicket> adminTickets(long page, long size) {
        return ticketMapper.selectPage(Page.of(page, size), null);
    }

    public VpsTicket adminReply(Long id, TicketMessageRequest request) {
        VpsTicket ticket = requiredTicket(id);
        addMessage(id, CurrentUser.id(), UserRole.ADMIN, request.content());
        ticket.setStatus(TicketStatus.REPLIED);
        ticketMapper.updateById(ticket);
        return ticket;
    }

    public VpsTicket adminClose(Long id) {
        VpsTicket ticket = requiredTicket(id);
        ticket.setStatus(TicketStatus.CLOSED);
        ticketMapper.updateById(ticket);
        return ticket;
    }

    private VpsTicket requiredMyTicket(Long id) {
        VpsTicket ticket = requiredTicket(id);
        if (!ticket.getUserId().equals(CurrentUser.id())) {
            throw new BizException(404, "工单不存在");
        }
        return ticket;
    }

    private VpsTicket requiredTicket(Long id) {
        VpsTicket ticket = ticketMapper.selectById(id);
        if (ticket == null) {
            throw new BizException(404, "工单不存在");
        }
        return ticket;
    }

    private java.util.List<VpsTicketMessage> messages(Long ticketId) {
        return messageMapper.selectList(new LambdaQueryWrapper<VpsTicketMessage>()
                .eq(VpsTicketMessage::getTicketId, ticketId)
                .orderByAsc(VpsTicketMessage::getId));
    }

    private void addMessage(Long ticketId, Long senderId, UserRole senderRole, String content) {
        VpsTicketMessage message = new VpsTicketMessage();
        message.setTicketId(ticketId);
        message.setSenderId(senderId);
        message.setSenderRole(senderRole);
        message.setContent(content);
        messageMapper.insert(message);
    }
}
