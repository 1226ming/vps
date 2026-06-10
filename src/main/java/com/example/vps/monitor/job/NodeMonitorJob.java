package com.example.vps.monitor.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NodeMonitorJob {
    @Scheduled(fixedDelay = 60000)
    public void collectNodeMetrics() {
        // 第一版预留：通过 sshj 执行 nproc/free/df/uptime/cat /proc/net/dev 并写入监控与流量表。
    }
}
