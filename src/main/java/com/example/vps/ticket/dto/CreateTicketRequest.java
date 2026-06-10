package com.example.vps.ticket.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketRequest(@NotBlank String title, String category, @NotBlank String content) {
}
