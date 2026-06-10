package com.example.vps.plan.controller;

import com.example.vps.common.result.Result;
import com.example.vps.plan.dto.PlanRequest;
import com.example.vps.plan.entity.VpsPlan;
import com.example.vps.plan.service.PlanService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PlanController {
    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/plans")
    public Result<List<VpsPlan>> plans() {
        return Result.success(planService.list());
    }

    @GetMapping("/plans/{id}")
    public Result<VpsPlan> detail(@PathVariable Long id) {
        return Result.success(planService.detail(id));
    }

    @PostMapping("/admin/plans")
    public Result<VpsPlan> create(@Valid @RequestBody PlanRequest request) {
        return Result.success(planService.create(request));
    }

    @PutMapping("/admin/plans/{id}")
    public Result<VpsPlan> update(@PathVariable Long id, @Valid @RequestBody PlanRequest request) {
        return Result.success(planService.update(id, request));
    }

    @DeleteMapping("/admin/plans/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        planService.delete(id);
        return Result.success();
    }
}
