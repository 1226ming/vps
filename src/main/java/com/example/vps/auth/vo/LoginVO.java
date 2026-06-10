package com.example.vps.auth.vo;

import com.example.vps.user.vo.UserProfileVO;

public record LoginVO(String token, UserProfileVO user) {
}
