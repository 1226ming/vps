package com.example.vps.ticket.dto;

import jakarta.validation.constraints.NotBlank;

public record TicketMessageRequest(@NotBlank String content) {
}
