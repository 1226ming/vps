package com.example.vps.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.vps.auth.dto.LoginRequest;
import com.example.vps.auth.dto.RegisterRequest;
import com.example.vps.auth.service.AuthService;
import com.example.vps.auth.vo.LoginVO;
import com.example.vps.common.result.Result;
import com.example.vps.user.dto.UpdatePasswordRequest;
import com.example.vps.user.dto.UpdateProfileRequest;
import com.example.vps.user.dto.UpdateUserStatusRequest;
import com.example.vps.user.entity.SysUser;
import com.example.vps.user.service.UserService;
import com.example.vps.user.vo.UserProfileVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
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

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(authService.register(request));
    }
}
