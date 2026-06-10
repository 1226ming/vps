package com.example.vps.monitor.service;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SshMonitorClient {
    public Map<String, Object> collect(String host, int port, String username, String password) {
        return Map.of("host", host, "port", port, "username", username, "connected", false);
    }
}
