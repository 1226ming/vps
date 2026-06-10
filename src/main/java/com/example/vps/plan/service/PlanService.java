package com.example.vps.plan.service;

import com.example.vps.common.exception.BizException;
import com.example.vps.plan.dto.PlanRequest;
import com.example.vps.plan.entity.VpsPlan;
import com.example.vps.plan.mapper.VpsPlanMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    private final VpsPlanMapper planMapper;

    public PlanService(VpsPlanMapper planMapper) {
        this.planMapper = planMapper;
    }

    public List<VpsPlan> list() {
        return planMapper.selectList(null);
    }

    public VpsPlan detail(Long id) {
        VpsPlan plan = planMapper.selectById(id);
        if (plan == null) {
            throw new BizException(404, "套餐不存在");
        }
        return plan;
    }

    public VpsPlan create(PlanRequest request) {
        VpsPlan plan = apply(new VpsPlan(), request);
        planMapper.insert(plan);
        return plan;
    }

    public VpsPlan update(Long id, PlanRequest request) {
        VpsPlan plan = detail(id);
        apply(plan, request);
        planMapper.updateById(plan);
        return plan;
    }

    public void delete(Long id) {
        planMapper.deleteById(id);
    }

    private VpsPlan apply(VpsPlan plan, PlanRequest request) {
        plan.setName(request.name());
        plan.setCpuCore(request.cpuCore());
        plan.setMemoryMb(request.memoryMb());
        plan.setDiskGb(request.diskGb());
        plan.setBandwidthMbps(request.bandwidthMbps());
        plan.setTrafficGb(request.trafficGb());
        plan.setPrice(request.price());
        plan.setDescription(request.description());
        plan.setEnabled(request.enabled() == null || request.enabled());
        return plan;
    }
}
