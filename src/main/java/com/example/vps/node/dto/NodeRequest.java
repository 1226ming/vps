package com.example.vps.node.dto;

import com.example.vps.common.enums.NodeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NodeRequest(@NotBlank String name, @NotBlank String host, @NotNull Integer sshPort,
                          @NotBlank String sshUsername, String sshPasswordEncrypted, Integer cpuCore,
                          Integer memoryMb, Integer diskGb, NodeStatus status) {
}
