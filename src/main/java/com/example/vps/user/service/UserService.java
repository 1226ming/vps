package com.example.vps.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vps.common.exception.BizException;
import com.example.vps.common.security.CurrentUser;
import com.example.vps.user.dto.UpdatePasswordRequest;
import com.example.vps.user.dto.UpdateProfileRequest;
import com.example.vps.user.dto.UpdateUserStatusRequest;
import com.example.vps.user.entity.SysUser;
import com.example.vps.user.mapper.SysUserMapper;
import com.example.vps.user.vo.UserProfileVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(SysUserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfileVO profile() {
        return toProfile(requiredUser(CurrentUser.id()));
    }

    public UserProfileVO updateProfile(UpdateProfileRequest request) {
        SysUser user = requiredUser(CurrentUser.id());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        userMapper.updateById(user);
        return toProfile(user);
    }

    public void updatePassword(UpdatePasswordRequest request) {
        SysUser user = requiredUser(CurrentUser.id());
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new BizException(400, "旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userMapper.updateById(user);
    }

    public IPage<SysUser> adminPage(long page, long size) {
        return userMapper.selectPage(Page.of(page, size), null);
    }

    public void updateStatus(Long id, UpdateUserStatusRequest request) {
        SysUser user = requiredUser(id);
        user.setStatus(request.status());
        userMapper.updateById(user);
    }

    private SysUser requiredUser(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            throw new BizException(404, "用户不存在");
        }
        return user;
    }

    private UserProfileVO toProfile(SysUser user) {
        return new UserProfileVO(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getRole(),
                user.getStatus(), user.getBalance());
    }
}
