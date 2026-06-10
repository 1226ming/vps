package com.example.vps.ticket.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.vps.common.result.Result;
import com.example.vps.ticket.dto.CreateTicketRequest;
import com.example.vps.ticket.dto.TicketMessageRequest;
import com.example.vps.ticket.entity.VpsTicket;
import com.example.vps.ticket.service.TicketService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/tickets")
    public Result<VpsTicket> create(@Valid @RequestBody CreateTicketRequest request) {
        return Result.success(ticketService.create(request));
    }

    @GetMapping("/tickets")
    public Result<IPage<VpsTicket>> myTickets(@RequestParam(defaultValue = "1") long page,
                                              @RequestParam(defaultValue = "20") long size) {
        return Result.success(ticketService.myTickets(page, size));
    }

    @GetMapping("/tickets/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.success(ticketService.detail(id));
    }

    @PostMapping("/tickets/{id}/messages")
    public Result<VpsTicket> reply(@PathVariable Long id, @Valid @RequestBody TicketMessageRequest request) {
        return Result.success(ticketService.reply(id, request));
    }

    @PostMapping("/tickets/{id}/close")
    public Result<VpsTicket> close(@PathVariable Long id) {
        return Result.success(ticketService.close(id));
    }

    @GetMapping("/admin/tickets")
    public Result<IPage<VpsTicket>> adminTickets(@RequestParam(defaultValue = "1") long page,
                                                 @RequestParam(defaultValue = "20") long size) {
        return Result.success(ticketService.adminTickets(page, size));
    }

    @PostMapping("/admin/tickets/{id}/reply")
    public Result<VpsTicket> adminReply(@PathVariable Long id, @Valid @RequestBody TicketMessageRequest request) {
        return Result.success(ticketService.adminReply(id, request));
    }

    @PostMapping("/admin/tickets/{id}/close")
    public Result<VpsTicket> adminClose(@PathVariable Long id) {
        return Result.success(ticketService.adminClose(id));
    }
}
