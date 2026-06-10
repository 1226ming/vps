package com.example.vps.user.vo;

import com.example.vps.common.enums.UserRole;
import com.example.vps.common.enums.UserStatus;
import java.math.BigDecimal;

public record UserProfileVO(Long id, String username, String email, String phone, UserRole role, UserStatus status,
                            BigDecimal balance) {
}
