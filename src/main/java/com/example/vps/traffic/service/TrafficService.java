package com.example.vps.traffic.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vps.common.exception.BizException;
import com.example.vps.common.security.CurrentUser;
import com.example.vps.instance.entity.VpsInstance;
import com.example.vps.instance.mapper.VpsInstanceMapper;
import com.example.vps.traffic.entity.VpsTrafficDetail;
import com.example.vps.traffic.mapper.VpsTrafficDetailMapper;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TrafficService {
    private final VpsTrafficDetailMapper trafficDetailMapper;
    private final VpsInstanceMapper instanceMapper;

    public TrafficService(VpsTrafficDetailMapper trafficDetailMapper, VpsInstanceMapper instanceMapper) {
        this.trafficDetailMapper = trafficDetailMapper;
        this.instanceMapper = instanceMapper;
    }

    public IPage<VpsTrafficDetail> my(LocalDate startTime, LocalDate endTime, long page, long size) {
        QueryWrapper<VpsTrafficDetail> query = new QueryWrapper<VpsTrafficDetail>()
                .eq("user_id", CurrentUser.id())
                .orderByDesc("collect_time");
        if (startTime != null) {
            query.ge("collect_time", startTime.atStartOfDay());
        }
        if (endTime != null) {
            query.le("collect_time", endTime.plusDays(1).atStartOfDay());
        }
        return trafficDetailMapper.selectPage(Page.of(page, size), query);
    }

    public IPage<VpsTrafficDetail> instanceTraffic(Long instanceId, long page, long size) {
        VpsInstance instance = instanceMapper.selectById(instanceId);
        if (instance == null || !instance.getUserId().equals(CurrentUser.id())) {
            throw new BizException(404, "VPS 实例不存在");
        }
        return trafficDetailMapper.selectPage(Page.of(page, size), new QueryWrapper<VpsTrafficDetail>()
                .eq("instance_id", instanceId)
                .orderByDesc("collect_time"));
    }

    public Map<String, Object> summary(Long instanceId) {
        VpsInstance instance = instanceMapper.selectById(instanceId);
        if (instance == null || !instance.getUserId().equals(CurrentUser.id())) {
            throw new BizException(404, "VPS 实例不存在");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("instanceId", instanceId);
        result.put("trafficUsedGb", instance.getTrafficUsedGb());
        result.put("trafficLimitGb", instance.getTrafficLimitGb());
        return result;
    }

    public IPage<VpsTrafficDetail> admin(long page, long size) {
        return trafficDetailMapper.selectPage(Page.of(page, size), null);
    }
}
