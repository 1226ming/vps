package com.example.vps.instance.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vps.common.enums.InstanceStatus;
import com.example.vps.common.enums.ServerStatus;
import com.example.vps.common.exception.BizException;
import com.example.vps.common.security.CurrentUser;
import com.example.vps.instance.dto.CreateVpnInstanceRequest;
import com.example.vps.instance.entity.VpsInstance;
import com.example.vps.instance.mapper.VpsInstanceMapper;
import com.example.vps.plan.entity.VpsPlan;
import com.example.vps.plan.mapper.VpsPlanMapper;
import com.example.vps.server.entity.VpsServer;
import com.example.vps.server.mapper.VpsServerMapper;
import com.example.vps.user.entity.SysUser;
import com.example.vps.user.mapper.SysUserMapper;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstanceService {
    private final VpsInstanceMapper instanceMapper;
    private final SysUserMapper userMapper;
    private final VpsPlanMapper planMapper;
    private final VpsServerMapper serverMapper;

    public InstanceService(VpsInstanceMapper instanceMapper, SysUserMapper userMapper, VpsPlanMapper planMapper,
                           VpsServerMapper serverMapper) {
        this.instanceMapper = instanceMapper;
        this.userMapper = userMapper;
        this.planMapper = planMapper;
        this.serverMapper = serverMapper;
    }

    public IPage<VpsInstance> myInstances(long page, long size) {
        return instanceMapper.selectPage(Page.of(page, size), new QueryWrapper<VpsInstance>()
                .eq("user_id", CurrentUser.id())
                .orderByDesc("id"));
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

    @Transactional
    public VpsInstance adminCreateVpn(CreateVpnInstanceRequest request) {
        SysUser user = userMapper.selectById(request.userId());
        if (user == null) {
            throw new BizException(404, "用户不存在");
        }
        VpsPlan plan = planMapper.selectById(request.planId());
        if (plan == null) {
            throw new BizException(404, "套餐不存在");
        }
        VpsServer server = serverMapper.selectById(request.serverId());
        if (server == null) {
            throw new BizException(404, "服务器不存在");
        }
        if (server.getStatus() != ServerStatus.AVAILABLE) {
            throw new BizException(409, "服务器当前不可用");
        }

        VpsInstance instance = new VpsInstance();
        instance.setUserId(user.getId());
        instance.setPlanId(plan.getId());
        instance.setServerId(server.getId());
        instance.setName(request.name());
        instance.setIpAddress(request.ipAddress());
        instance.setVpnPort(request.vpnPort());
        instance.setVpnProtocol(request.vpnProtocol() == null ? "OPENVPN" : request.vpnProtocol());
        instance.setOsName(request.osName());
        instance.setStatus(InstanceStatus.RUNNING);
        instance.setTrafficUsedGb(BigDecimal.ZERO);
        instance.setTrafficLimitGb(request.trafficLimitGb() == null
                ? BigDecimal.valueOf(plan.getTrafficGb())
                : request.trafficLimitGb());
        instance.setExpireTime(request.expireTime());
        instanceMapper.insert(instance);
        return instance;
    }

    public IPage<VpsInstance> adminInstances(long page, long size) {
        return instanceMapper.selectPage(Page.of(page, size), null);
    }
}
