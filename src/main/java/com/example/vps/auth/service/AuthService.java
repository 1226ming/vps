package com.example.vps.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.vps.auth.dto.LoginRequest;
import com.example.vps.auth.dto.RegisterRequest;
import com.example.vps.auth.vo.LoginVO;
import com.example.vps.common.enums.UserRole;
import com.example.vps.common.enums.UserStatus;
import com.example.vps.common.exception.BizException;
import com.example.vps.common.security.CurrentUser;
import com.example.vps.common.security.JwtService;
import com.example.vps.user.entity.SysUser;
import com.example.vps.user.mapper.SysUserMapper;
import com.example.vps.user.vo.UserProfileVO;
import java.math.BigDecimal;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(SysUserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginVO register(RegisterRequest request) {
        SysUser existing = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.username()));
        if (existing != null) {
            throw new BizException(409, "用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.NORMAL);
        user.setBalance(BigDecimal.ZERO);
        userMapper.insert(user);
        return issueToken(user);
    }

    public LoginVO login(LoginRequest request) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.username()));
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("bad credentials");
        }
        if (user.getStatus() != UserStatus.NORMAL) {
            throw new BizException(403, "用户不可用");
        }
        return issueToken(user);
    }

    public UserProfileVO me() {
        SysUser user = userMapper.selectById(CurrentUser.id());
        if (user == null) {
            throw new BizException(404, "用户不存在");
        }
        return toProfile(user);
    }

    private LoginVO issueToken(SysUser user) {
        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole().name());
        return new LoginVO(token, toProfile(user));
    }

    private UserProfileVO toProfile(SysUser user) {
        return new UserProfileVO(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getRole(),
                user.getStatus(), user.getBalance());
    }
}
