package com.example.vps.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.vps.common.result.Result;
import com.example.vps.user.dto.UpdatePasswordRequest;
import com.example.vps.user.dto.UpdateProfileRequest;
import com.example.vps.user.dto.UpdateUserStatusRequest;
import com.example.vps.user.entity.SysUser;
import com.example.vps.user.service.UserService;
import com.example.vps.user.vo.UserProfileVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/profile")
    public Result<UserProfileVO> profile() {
        return Result.success(userService.profile());
    }

    @PutMapping("/users/profile")
    public Result<UserProfileVO> updateProfile(@RequestBody UpdateProfileRequest request) {
        return Result.success(userService.updateProfile(request));
    }

    @PutMapping("/users/password")
    public Result<Void> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(request);
        return Result.success();
    }

    @GetMapping("/admin/users")
    public Result<IPage<SysUser>> adminUsers(@RequestParam(defaultValue = "1") long page,
                                             @RequestParam(defaultValue = "20") long size) {
        return Result.success(userService.adminPage(page, size));
    }

    @PutMapping("/admin/users/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateUserStatusRequest request) {
        userService.updateStatus(id, request);
        return Result.success();
    }
}
