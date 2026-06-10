# VPS 管理平台后端

基于《VPS 管理平台-后端设计文档》和《VPS 管理平台-后端接口设计文档》搭建的 Spring Boot 单体模块化项目。

## 技术栈

- Spring Boot 3 + Spring MVC
- Spring Security + JWT
- MyBatis-Plus
- MySQL
- Redis
- sshj
- Scheduled 监控采集任务

## IDEA 打开方式

1. 使用 IntelliJ IDEA 打开当前目录 `/Users/yuyaodong/Documents/VPS`
2. 选择以 Maven 项目导入，等待依赖同步
3. 修改 `src/main/resources/application.yml` 中的 MySQL、Redis 配置
4. 执行 `src/main/resources/db/schema.sql` 初始化数据库
5. 运行 `com.example.vps.VpsApplication`

## 默认接口前缀

全部接口统一使用 `/api` 前缀；公开接口为注册、登录、套餐查询，其余业务接口需要 Bearer Token，后台接口需要 `ADMIN` 权限。
