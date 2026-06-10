package com.example.vps.node.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vps.common.enums.NodeStatus;
import com.example.vps.common.exception.BizException;
import com.example.vps.monitor.entity.VpsNodeMonitor;
import com.example.vps.monitor.mapper.VpsNodeMonitorMapper;
import com.example.vps.node.dto.NodeRequest;
import com.example.vps.node.entity.VpsNode;
import com.example.vps.node.mapper.VpsNodeMapper;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class NodeService {
    private final VpsNodeMapper nodeMapper;
    private final VpsNodeMonitorMapper monitorMapper;

    public NodeService(VpsNodeMapper nodeMapper, VpsNodeMonitorMapper monitorMapper) {
        this.nodeMapper = nodeMapper;
        this.monitorMapper = monitorMapper;
    }

    public IPage<VpsNode> list(long page, long size) {
        return nodeMapper.selectPage(Page.of(page, size), null);
    }

    public VpsNode detail(Long id) {
        VpsNode node = nodeMapper.selectById(id);
        if (node == null) {
            throw new BizException(404, "节点不存在");
        }
        return node;
    }

    public VpsNode create(NodeRequest request) {
        VpsNode node = apply(new VpsNode(), request);
        nodeMapper.insert(node);
        return node;
    }

    public VpsNode update(Long id, NodeRequest request) {
        VpsNode node = detail(id);
        apply(node, request);
        nodeMapper.updateById(node);
        return node;
    }

    public void delete(Long id) {
        nodeMapper.deleteById(id);
    }

    public Map<String, Object> check(Long id) {
        VpsNode node = detail(id);
        return Map.of("nodeId", node.getId(), "host", node.getHost(), "connected", false, "message", "SSH 检测待接入");
    }

    public IPage<VpsNodeMonitor> monitor(Long id, long page, long size) {
        detail(id);
        return monitorMapper.selectPage(Page.of(page, size), null);
    }

    private VpsNode apply(VpsNode node, NodeRequest request) {
        node.setName(request.name());
        node.setHost(request.host());
        node.setSshPort(request.sshPort());
        node.setSshUsername(request.sshUsername());
        node.setSshPasswordEncrypted(request.sshPasswordEncrypted());
        node.setCpuCore(request.cpuCore());
        node.setMemoryMb(request.memoryMb());
        node.setDiskGb(request.diskGb());
        node.setStatus(request.status() == null ? NodeStatus.OFFLINE : request.status());
        return node;
    }
}
