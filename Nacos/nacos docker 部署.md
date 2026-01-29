
## 一、整体架构先定下来

```
┌─────────────┐        JDBC      ┌─────────────┐
│  Nacos 2.4.1│ ───────────────▶ │  MySQL 8.0  │
│  8848 / 8080│                  │ nacos_config│
└─────────────┘                  └─────────────┘
```

* **Nacos：配置中心 + 注册中心**
* **MySQL：持久化配置 / 服务元数据**
* 模式：`standalone + external DB`
* 不用 Derby（生产禁用）

---

## 二、准备 MySQL 初始化 SQL（关键）

### 1 SQL 从哪里来？

官方仓库里有：
```bash
wget -O mysql-schema.sql https://raw.githubusercontent.com/alibaba/nacos/refs/tags/2.4.1/distribution/conf/mysql-schema.sql?utm_source=chatgpt.com
```

接从 GitHub 对应版本下载。
```bash
git clone https://github.com/nacos-group/nacos-docker.git
```
---

### 2 目录结构建议

```bash
nacos-docker/
├── docker-compose.yml
└── init/
    └── nacos-mysql.sql
```

---

## 三、docker-compose.yml（完整版，推荐用这个）

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: nacos123
      MYSQL_DATABASE: nacos_config
      TZ: Asia/Tokyo
    ports:
      - "3306:3306"
    volumes:
      - ./mysql_data:/var/lib/mysql
      - ./init:/docker-entrypoint-initdb.d
    command:
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_unicode_ci
      --default-authentication-plugin=mysql_native_password

  nacos:
    image: nacos/nacos-server:v2.4.1
    container_name: nacos
    restart: always
    depends_on:
      - mysql
    ports:
      - "8848:8848"
      - "9848:9848"
    environment:
      - MODE=standalone

      # JVM
      - JVM_XMS=512m
      - JVM_XMX=512m
      - JVM_XMN=256m

      # 数据库
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=mysql
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_DB_NAME=nacos_config
      - MYSQL_SERVICE_USER=nacos
      - MYSQL_SERVICE_PASSWORD=nacos123

      # 鉴权
      - NACOS_AUTH_ENABLE=true
      - NACOS_AUTH_IDENTITY_KEY=nacos
      - NACOS_AUTH_IDENTITY_VALUE=nacos
      - NACOS_AUTH_TOKEN=kYcYp5Q+W8sL1HfZ9E9Q9Z7eHn4Cz6JqM0uQe8N0a0c=
      - NACOS_AUTH_TOKEN_EXPIRE_SECONDS=18000

      - TZ=Asia/Tokyo

```
```bash
openssl rand -base64 32

```
在nacos-mysql.sql 中添加以下内容，添加账号：
```bash
-- 创建 nacos 用户（如果不存在）
CREATE USER IF NOT EXISTS 'nacos'@'%' IDENTIFIED BY 'nacos123';

-- 授权 nacos_config 数据库
GRANT ALL PRIVILEGES ON nacos_config.* TO 'nacos'@'%';

```
---

## 四、启动顺序（别跳）

```bash
docker compose up -d
```

然后观察日志：

```bash
docker logs -f nacos

INFO Nacos started successfully in stand alone mode. use external storage
```

你要看到类似：

```
Datasource: mysql
Nacos started successfully
```

不是这句，说明数据库没连上。

---

## 五、访问 & 登录

### 1️⃣ Web 控制台

```
http://宿主机IP:8848/nacos
```

### 2️⃣ 默认账号

```
username: nacos
password: nacos
```

（启用鉴权后第一次仍然是这个）

---

发布配置：
```bash
curl -X POST "http://100.100.3.147:8848/nacos/v1/cs/configs?dataId=demo-svc&group=DEFAULT_GROUP&content=server.port=8081"

curl -X POST "http://100.100.3.147:8848/nacos/v1/cs/configs" \
  -d "dataId=demo-svc.properties" \
  -d "group=DEFAULT_GROUP" \
  -d "content=server.port=8081
test.value=hello-from-curl" \
  -d "type=properties" \
  -d "accessToken=你的token"

```
获取配置：
```bash
curl -X GET "http://100.100.3.147:8848/nacos/v1/cs/configs?dataId=demo-svc&group=DEFAULT_GROUP" 
{"timestamp":"2026-01-29T17:34:13.957+09:00","status":403,"error":"Forbidden","message":"user not found!","path":"/nacos/v1/cs/configs"}

curl -G "http://100.100.3.147:8848/nacos/v1/cs/configs" \
  --data-urlencode "dataId=demo-svc" \
  --data-urlencode "group=DEFAULT_GROUP" \
  --data-urlencode "accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTc2OTY5NTEzNn0.kKFpCuIxlJ6VLTOBxWinwpRv7YxBwqGbkj_Cn_pUFl8"

```
```bash
curl -X POST "http://100.100.3.147:8848/nacos/v1/auth/login"  -d "username=nacos&password=nacos"
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTc2OTY5NTEzNn0.kKFpCuIxlJ6VLTOBxWinwpRv7YxBwqGbkj_Cn_pUFl8
```

## 六、验证是否真的在用 MySQL（重要）

进 MySQL 容器：

```bash
docker exec -it nacos-mysql mysql -unacos -pnacos123 nacos_config
```

查看表：

```sql
show tables;
```

看到 `config_info`, `his_config_info`, `tenant_info` 等，说明成功。

---


客户端application.properties 配置
```t
spring.application.name=demo-svc

# Boot 2.4+ 必须
spring.config.import=nacos:

# Nacos 地址（注意不是 config.server-addr）
spring.cloud.nacos.server-addr=100.100.3.147:8848

# 鉴权
spring.cloud.nacos.username=nacos
spring.cloud.nacos.password=nacos

# 配置中心
spring.cloud.nacos.config.enabled=true
spring.cloud.nacos.config.file-extension=properties
spring.cloud.nacos.config.group=DEFAULT_GROUP
spring.cloud.nacos.config.namespace=public

```
## 七、为什么生产一定要 MySQL？

一句话版：

> **不用 MySQL，Nacos 就是“健忘症患者” **

技术原因：

* Derby 是内存数据库
* 重启配置全丢
* 多实例无法共享数据
* 官方明确：生产禁用

---

## 八、你之前问过的几个坑，这里一次性点名 ⚠️

### ❌ 1. UI 新建配置，服务读不到

👉 99% 是 **没连 MySQL，用的 Derby**

---

### ❌ 2. Nacos 起不来

常见原因：

* 没初始化 `nacos-mysql.sql`
* MySQL 8 认证插件没设 `mysql_native_password`
* 密码包含特殊字符未转义

---

### ❌ 3. 端口 confusion

* **8848**：HTTP API + 控制台
* **9848**：gRPC client
* **9849**：gRPC server

---

## 九、如果你想再进阶

我可以直接给你：

* ✅ **三节点 Nacos 集群 + MySQL**
* ✅ **Nacos + MySQL + Redis + Kafka 架构图**
* ✅ **Spring Boot + Nacos + MySQL 配置完整示例**
* ✅ **K8s StatefulSet 版本**

你说一个，我直接展开，不留坑。
