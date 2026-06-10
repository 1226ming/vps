package com.example.vps.node.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.vps.common.result.Result;
import com.example.vps.monitor.entity.VpsNodeMonitor;
import com.example.vps.node.dto.NodeRequest;
import com.example.vps.node.entity.VpsNode;
import com.example.vps.node.service.NodeService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/nodes")
public class NodeController {
    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public Result<IPage<VpsNode>> list(@RequestParam(defaultValue = "1") long page,
                                       @RequestParam(defaultValue = "20") long size) {
        return Result.success(nodeService.list(page, size));
    }

    @PostMapping
    public Result<VpsNode> create(@Valid @RequestBody NodeRequest request) {
        return Result.success(nodeService.create(request));
    }

    @GetMapping("/{id}")
    public Result<VpsNode> detail(@PathVariable Long id) {
        return Result.success(nodeService.detail(id));
    }

    @PutMapping("/{id}")
    public Result<VpsNode> update(@PathVariable Long id, @Valid @RequestBody NodeRequest request) {
        return Result.success(nodeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        nodeService.delete(id);
        return Result.success();
    }

    @PostMapping("/{id}/check")
    public Result<Map<String, Object>> check(@PathVariable Long id) {
        return Result.success(nodeService.check(id));
    }

    @GetMapping("/{id}/monitor")
    public Result<IPage<VpsNodeMonitor>> monitor(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "1") long page,
                                                 @RequestParam(defaultValue = "20") long size) {
        return Result.success(nodeService.monitor(id, page, size));
    }
}
