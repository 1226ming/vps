package com.example.vps.instance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.vps.common.enums.InstanceStatus;
import com.example.vps.common.result.Result;
import com.example.vps.instance.dto.CreateVpnInstanceRequest;
import com.example.vps.instance.entity.VpsInstance;
import com.example.vps.instance.service.InstanceService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class InstanceController {
    private final InstanceService instanceService;

    public InstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @GetMapping("/instances")
    public Result<IPage<VpsInstance>> myInstances(@RequestParam(defaultValue = "1") long page,
                                                  @RequestParam(defaultValue = "20") long size) {
        return Result.success(instanceService.myInstances(page, size));
    }

    @GetMapping("/instances/{id}")
    public Result<VpsInstance> detail(@PathVariable Long id) {
        return Result.success(instanceService.myDetail(id));
    }

    @PostMapping("/instances/{id}/start")
    public Result<VpsInstance> start(@PathVariable Long id) {
        return Result.success(instanceService.action(id, InstanceStatus.RUNNING));
    }

    @PostMapping("/instances/{id}/stop")
    public Result<VpsInstance> stop(@PathVariable Long id) {
        return Result.success(instanceService.action(id, InstanceStatus.STOPPED));
    }

    @PostMapping("/instances/{id}/restart")
    public Result<VpsInstance> restart(@PathVariable Long id) {
        return Result.success(instanceService.action(id, InstanceStatus.RUNNING));
    }

    @PostMapping("/instances/{id}/reinstall")
    public Result<VpsInstance> reinstall(@PathVariable Long id) {
        return Result.success(instanceService.action(id, InstanceStatus.CREATING));
    }

    @PostMapping("/instances/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        instanceService.myDetail(id);
        return Result.success();
    }

    @GetMapping("/instances/{id}/monitor")
    public Result<Map<String, Object>> monitor(@PathVariable Long id) {
        return Result.success(instanceService.monitor(id));
    }

    @GetMapping("/admin/instances")
    public Result<IPage<VpsInstance>> adminInstances(@RequestParam(defaultValue = "1") long page,
                                                     @RequestParam(defaultValue = "20") long size) {
        return Result.success(instanceService.adminInstances(page, size));
    }

    @PostMapping("/admin/instances")
    public Result<VpsInstance> adminCreateVpn(@Valid @RequestBody CreateVpnInstanceRequest request) {
        return Result.success(instanceService.adminCreateVpn(request));
    }
}
