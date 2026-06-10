package com.example.vps.plan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PlanRequest(@NotBlank String name, @NotNull Integer cpuCore, @NotNull Integer memoryMb,
                          @NotNull Integer diskGb, @NotNull Integer bandwidthMbps, @NotNull Integer trafficGb,
                          @NotNull BigDecimal price, String description, Boolean enabled) {
}
