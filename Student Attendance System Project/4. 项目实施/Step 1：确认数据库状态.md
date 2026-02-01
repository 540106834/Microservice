好的，我们来**逐字段拆解**你这张 `attendance_raw` 表，把每个字段的作用、为什么选这个数据类型，以及设计背后的逻辑都讲清楚。
这是工程级必备思维：看到字段就能理解整个业务行为。

```bash
CREATE DATABASE IF NOT EXISTS smart_attendance 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_general_ci;
use smart_attendane;


CREATE USER 'attend'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON smart_attendance.* TO 'attend'@'%';
SHOW GRANTS FOR 'attend'@'%';

-- 刷新权限，使设置立即生效
FLUSH PRIVILEGES;
```

---

# 📝 表结构总览

```sql
CREATE TABLE attendance_raw (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '自增主键',

    device_sn VARCHAR(64) NOT NULL COMMENT '设备ID / 序列号',
    card_no VARCHAR(32) NOT NULL COMMENT '学生卡号',
    event_time DATETIME NOT NULL COMMENT '打卡时间',
    event_type TINYINT NOT NULL COMMENT '0进校 1离校',
    sn VARCHAR(16) NOT NULL COMMENT '设备上传流水号',

    parsed TINYINT DEFAULT 0 COMMENT '是否解析，0未解析 1已解析',
    received_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '接收时间',

    UNIQUE KEY uk_device_sn (device_sn, sn),
    INDEX idx_card_time (card_no, event_time)
);
```

---

# 1️⃣ `id BIGINT PRIMARY KEY AUTO_INCREMENT`

* **作用**：每条原始打卡数据的唯一标识，自增 ID。
* **数据类型**：`BIGINT`

  * 范围大（0 ~ 9,223,372,036,854,775,807），够整个学校系统用几十年。
* **AUTO_INCREMENT**：数据库自动生成，无需手动维护。
* **PRIMARY KEY**：

  * 唯一性保证
  * 建立聚簇索引，提高查询效率
* **设计意义**：基础主键，稳定、不可变，任何业务都可以引用这个 ID。

---

# 2️⃣ `device_sn VARCHAR(64) NOT NULL`

* **作用**：标识来源设备。每台闸机、刷卡机、人脸机都有唯一 SN。
* **数据类型**：`VARCHAR(64)`

  * 原因：设备 SN 可能是数字、字母组合，长度不固定。
* **NOT NULL**：设备必须提供，否则数据没来源。
* **设计意义**：区分不同设备，是解析和排查的第一层关键信息。

---

# 3️⃣ `card_no VARCHAR(32) NOT NULL`

* **作用**：学生卡号，对应学生。
* **数据类型**：`VARCHAR(32)`

  * 一般卡号不超过 32 位，支持数字+字母。
* **NOT NULL**：每条打卡数据必须绑定学生，否则无法入库。
* **设计意义**：原始数据与学生身份的桥梁。

---

# 4️⃣ `event_time DATETIME NOT NULL`

* **作用**：学生实际刷卡时间。
* **数据类型**：`DATETIME`

  * 精确到秒，方便判断是否迟到/早退。
* **NOT NULL**：数据无时间就无法解析考勤。
* **设计意义**：核心时间字段，考勤计算完全依赖它。

---

# 5️⃣ `event_type TINYINT NOT NULL`

* **作用**：标识刷卡类型：进校 / 离校。
* **数据类型**：`TINYINT`

  * 小整数即可存储状态（0 = 进校，1 = 离校）。
* **设计意义**：区分上学和放学打卡，核心业务字段。

---

# 6️⃣ `sn VARCHAR(16) NOT NULL`

* **作用**：设备上传的流水号，每次上传唯一。
* **数据类型**：`VARCHAR(16)`

  * 一般设备流水号长度不大，16 足够。
* **NOT NULL**：每条记录必须有流水号，才能防重复。
* **设计意义**：结合 `device_sn` 形成唯一约束，防止重复上传。

---

# 7️⃣ `parsed TINYINT DEFAULT 0`

* **作用**：标记这条数据是否被解析。

  * `0` = 未解析
  * `1` = 已解析
* **数据类型**：`TINYINT`

  * 节省空间，存储状态即可。
* **默认值**：0，新数据默认未解析。
* **设计意义**：方便 parser 程序按状态拉取未解析数据，幂等处理。

---

# 8️⃣ `received_at DATETIME DEFAULT CURRENT_TIMESTAMP`

* **作用**：系统接收到原始数据的时间。
* **数据类型**：`DATETIME`

  * 自动记录时间，无需额外传参。
* **默认值**：`CURRENT_TIMESTAMP`，保证每条数据有时间戳。
* **设计意义**：

  * 排查延迟、异常
  * 对账和日志分析必备

---

# 9️⃣ 索引与唯一约束

### ✅ `UNIQUE KEY uk_device_sn (device_sn, sn)`

* **作用**：保证同一设备的同一条流水号不会重复入库。
* **意义**：

  * 防止网络重传
  * 防止设备重复上传同一条打卡记录
  * 提高数据幂等性

---

### ✅ `INDEX idx_card_time (card_no, event_time)`

* **作用**：按学生卡号和打卡时间查询记录。
* **意义**：

  * 用于计算考勤
  * 支撑报表和统计
* **设计原则**：

  * 查询字段放索引，提高性能
  * 避免扫描整个表

---

# 🔹 总结

1. 这张表**最小化**，只保留绝对必要字段：设备、学生、时间、类型、流水号。
2. 解析状态和接收时间保证可追溯、可幂等。
3. 唯一约束保证不会重复上传。
4. 索引保证查询效率，支撑业务闭环。

> 用这张表，你就有了 **最小可上线的原始打卡数据表**。
> 可以跑整个考勤系统链路了。

---

如果你需要，我可以帮你写**下一步解析流程**：

* 从 `attendance_raw` 到 `att_record`
* 生成上学/放学考勤记录
* 保证唯一、可追溯
* 只用最小字段

这样系统可以完整跑起来。

你希望我直接写吗？
