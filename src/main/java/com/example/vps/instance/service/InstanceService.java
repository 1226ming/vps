package com.example.vps.instance.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vps.common.enums.InstanceStatus;
import com.example.vps.common.exception.BizException;
import com.example.vps.common.security.CurrentUser;
import com.example.vps.instance.entity.VpsInstance;
import com.example.vps.instance.mapper.VpsInstanceMapper;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class InstanceService {
    private final VpsInstanceMapper instanceMapper;

    public InstanceService(VpsInstanceMapper instanceMapper) {
        this.instanceMapper = instanceMapper;
    }

    public IPage<VpsInstance> myInstances(long page, long size) {
        return instanceMapper.selectPage(Page.of(page, size), new LambdaQueryWrapper<VpsInstance>()
                .eq(VpsInstance::getUserId, CurrentUser.id()));
    }

    public VpsInstance myDetail(Long id) {
        VpsInstance instance = instanceMapper.selectById(id);
        if (instance == null || !instance.getUserId().equals(CurrentUser.id())) {
            throw new BizException(404, "VPS 实例不存在");
        }
        return instance;
    }

    public VpsInstance action(Long id, InstanceStatus targetStatus) {
        VpsInstance instance = myDetail(id);
        instance.setStatus(targetStatus);
        instanceMapper.updateById(instance);
        return instance;
    }

    public Map<String, Object> monitor(Long id) {
        VpsInstance instance = myDetail(id);
        return Map.of("instanceId", instance.getId(), "status", instance.getStatus(), "cpuUsage", 0,
                "memoryUsage", 0, "diskUsage", 0);
    }

    public IPage<VpsInstance> adminInstances(long page, long size) {
        return instanceMapper.selectPage(Page.of(page, size), null);
    }
}
