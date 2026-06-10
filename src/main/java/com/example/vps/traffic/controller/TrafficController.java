package com.example.vps.traffic.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.vps.common.result.Result;
import com.example.vps.traffic.entity.VpsTrafficDetail;
import com.example.vps.traffic.service.TrafficService;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TrafficController {
    private final TrafficService trafficService;

    public TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @GetMapping("/traffic/my")
    public Result<IPage<VpsTrafficDetail>> my(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endTime,
                                              @RequestParam(defaultValue = "1") long page,
                                              @RequestParam(defaultValue = "20") long size) {
        return Result.success(trafficService.my(startTime, endTime, page, size));
    }

    @GetMapping("/traffic/instance/{instanceId}")
    public Result<IPage<VpsTrafficDetail>> instanceTraffic(@PathVariable Long instanceId,
                                                           @RequestParam(defaultValue = "1") long page,
                                                           @RequestParam(defaultValue = "20") long size) {
        return Result.success(trafficService.instanceTraffic(instanceId, page, size));
    }

    @GetMapping("/instances/{id}/traffic/summary")
    public Result<Map<String, Object>> summary(@PathVariable Long id) {
        return Result.success(trafficService.summary(id));
    }

    @GetMapping("/admin/traffic")
    public Result<IPage<VpsTrafficDetail>> admin(@RequestParam(defaultValue = "1") long page,
                                                 @RequestParam(defaultValue = "20") long size) {
        return Result.success(trafficService.admin(page, size));
    }
}
