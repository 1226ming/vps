CREATE DATABASE IF NOT EXISTS vps_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE vps_platform;

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    email VARCHAR(128),
    phone VARCHAR(32),
    role VARCHAR(32) NOT NULL DEFAULT 'USER',
    status VARCHAR(32) NOT NULL DEFAULT 'NORMAL',
    balance DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS vps_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL UNIQUE,
    cpu_core INT NOT NULL,
    memory_mb INT NOT NULL,
    disk_gb INT NOT NULL,
    bandwidth_mbps INT NOT NULL,
    traffic_gb INT NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    description VARCHAR(512),
    enabled BIT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS vps_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    duration_days INT NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    status VARCHAR(32) NOT NULL,
    pay_time DATETIME,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_order_user_id (user_id)
);

CREATE TABLE IF NOT EXISTS vps_node (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    host VARCHAR(128) NOT NULL,
    ssh_port INT NOT NULL DEFAULT 22,
    ssh_username VARCHAR(64) NOT NULL,
    ssh_password_encrypted VARCHAR(512),
    cpu_core INT,
    memory_mb INT,
    disk_gb INT,
    status VARCHAR(32) NOT NULL DEFAULT 'OFFLINE',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS vps_server (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    host VARCHAR(128) NOT NULL,
    ip_address VARCHAR(64),
    ssh_port INT NOT NULL DEFAULT 22,
    ssh_username VARCHAR(64) NOT NULL,
    ssh_password_encrypted VARCHAR(512),
    provider VARCHAR(64),
    region VARCHAR(128),
    cpu_core INT,
    memory_mb INT,
    disk_gb INT,
    bandwidth_mbps INT,
    status VARCHAR(32) NOT NULL DEFAULT 'AVAILABLE',
    remark VARCHAR(512),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS vps_instance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    order_id BIGINT,
    plan_id BIGINT NOT NULL,
    node_id BIGINT,
    server_id BIGINT,
    name VARCHAR(128),
    ip_address VARCHAR(64),
    vpn_port INT,
    vpn_protocol VARCHAR(32),
    os_name VARCHAR(128),
    status VARCHAR(32) NOT NULL,
    traffic_used_gb DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    traffic_limit_gb DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    expire_time DATETIME,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_instance_user_id (user_id)
);

CREATE TABLE IF NOT EXISTS vps_node_monitor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    node_id BIGINT NOT NULL,
    cpu_usage DECIMAL(8, 2),
    memory_used_mb INT,
    memory_total_mb INT,
    disk_used_gb INT,
    disk_total_gb INT,
    load1 DECIMAL(8, 2),
    load5 DECIMAL(8, 2),
    load15 DECIMAL(8, 2),
    in_bytes BIGINT,
    out_bytes BIGINT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_monitor_node_id (node_id)
);

CREATE TABLE IF NOT EXISTS vps_traffic_snapshot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    instance_id BIGINT NOT NULL UNIQUE,
    last_in_bytes BIGINT NOT NULL DEFAULT 0,
    last_out_bytes BIGINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS vps_traffic_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    instance_id BIGINT NOT NULL,
    in_bytes BIGINT NOT NULL DEFAULT 0,
    out_bytes BIGINT NOT NULL DEFAULT 0,
    total_bytes BIGINT NOT NULL DEFAULT 0,
    collect_time DATETIME NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_traffic_user_id (user_id),
    INDEX idx_traffic_instance_id (instance_id),
    INDEX idx_traffic_collect_time (collect_time)
);

CREATE TABLE IF NOT EXISTS vps_ticket (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(128) NOT NULL,
    category VARCHAR(64),
    status VARCHAR(32) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_ticket_user_id (user_id)
);

CREATE TABLE IF NOT EXISTS vps_ticket_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    sender_role VARCHAR(32) NOT NULL,
    content TEXT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_ticket_message_ticket_id (ticket_id)
);

-- 默认管理员：admin / admin123，上线后请立即修改密码。
INSERT INTO sys_user (username, password, email, role, status, balance)
VALUES ('admin', '$2y$10$sy4OPD4S/JTB1nGko4QW5ORwTQjv8YjO3naU17wqaMgLNuJ9G1znC', 'admin@example.com', 'ADMIN', 'NORMAL', 0.00)
ON DUPLICATE KEY UPDATE username = username;

-- 内置 VPN 套餐数据，可重复执行，不会按套餐名重复插入。
INSERT IGNORE INTO vps_plan (
    name, cpu_core, memory_mb, disk_gb, bandwidth_mbps, traffic_gb, price, description, enabled
)
VALUES ('基础 VPN 套餐', 1, 1024, 20, 50, 100, 19.90, '适合轻量浏览、聊天和基础隐私保护。',
        1);

INSERT IGNORE INTO vps_plan (
    name, cpu_core, memory_mb, disk_gb, bandwidth_mbps, traffic_gb, price, description, enabled
)
VALUES ('高级 VPN 套餐', 2, 2048, 40, 100, 500, 49.90, '适合多设备、流媒体、P2P 和日常高频使用。',
        1);

INSERT IGNORE INTO vps_plan (
    name, cpu_core, memory_mb, disk_gb, bandwidth_mbps, traffic_gb, price, description, enabled
)
VALUES ('旗舰 VPN 套餐', 4, 4096, 80, 200, 1000, 99.90, '包含专属 IP、Double VPN、混淆协议和更高带宽。',
        1);

INSERT IGNORE INTO vps_plan (
    name, cpu_core, memory_mb, disk_gb, bandwidth_mbps, traffic_gb, price, description, enabled
)
VALUES ('企业 VPN 套餐', 8, 8192, 160, 500, 3000, 299.90, '适合团队使用，支持更多设备、专属 IP 和高级安全能力。',
        1);
