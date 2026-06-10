package com.example.vps.order.dto;

import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(@NotNull Long planId, @NotNull Integer durationDays) {
}
