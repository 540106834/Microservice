好，下面直接给你**可在 MySQL 8.x 上执行的核心 SQL 建表语句**。
我只挑**最关键、最容易踩坑、最能体现设计水平**的部分来写：
👉 **学生核心表 + 考勤核心表**。
不是教学 Demo，而是“你敢往生产扔”的版本。🧱

---

# 一、学生核心表 SQL

## 1️⃣ 学生主表 `stu_student`

```sql
CREATE TABLE stu_student (
  id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  student_code  VARCHAR(20) NOT NULL COMMENT '学生唯一编码',
  student_name  VARCHAR(32) NOT NULL COMMENT '学生姓名',
  gender        TINYINT     NOT NULL COMMENT '性别：1男 2女',
  birthday      DATE        DEFAULT NULL COMMENT '出生日期',
  status        TINYINT     NOT NULL DEFAULT 1 COMMENT '状态：1在读 2转学 3毕业',
  created_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY uk_student_code (student_code),
  KEY idx_student_name (student_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生主表';
```

### 🔍 设计要点

* `student_code` **唯一 + 永不修改**
* `status` 表示学籍状态，不删除数据
* 不把班级直接放这里，为转班留后路

---

## 2️⃣ 学生班级关系表 `stu_student_class`

```sql
CREATE TABLE stu_student_class (
  id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  student_code  VARCHAR(20) NOT NULL COMMENT '学生编码',
  class_code    VARCHAR(12) NOT NULL COMMENT '班级编码',
  start_date    DATE        NOT NULL COMMENT '入班日期',
  end_date      DATE        DEFAULT NULL COMMENT '离班日期',
  created_at    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY idx_student (student_code),
  KEY idx_class (class_code),
  KEY idx_active (student_code, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生-班级关系表';
```

### 🔍 设计要点

* **一人多条记录**，解决转班、留级
* `end_date IS NULL` = 当前所在班级
* 这是典型“时间轴关系表”

---

# 二、考勤核心表 SQL ⭐

## 3️⃣ 考勤记录表 `att_record`

```sql
CREATE TABLE att_record (
  id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  student_code   VARCHAR(20) NOT NULL COMMENT '学生编码',
  class_code     VARCHAR(12) NOT NULL COMMENT '班级编码',
  attend_date    DATE        NOT NULL COMMENT '考勤日期',
  attend_type    VARCHAR(8)  NOT NULL COMMENT '类型：IN上学 / OUT放学',
  attend_time    DATETIME    NOT NULL COMMENT '实际打卡时间',
  status_code    VARCHAR(4)  NOT NULL COMMENT '考勤状态编码',
  source         VARCHAR(16) DEFAULT 'CARD' COMMENT '来源：CARD/APP/GATE',
  created_at     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY uk_student_day_type (student_code, attend_date, attend_type),
  KEY idx_class_date (class_code, attend_date),
  KEY idx_student_date (student_code, attend_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生考勤记录表';
```

### 🚨 这是整套系统最重要的一张表

**为什么这么设计：**

* `student_code + date + type` 唯一
  👉 防止一天多次“上学”
* 不存 `迟到分钟数`
  👉 状态是结果，不是过程
* **禁止 delete**
* 允许补数据，但必须记日志

---

## 4️⃣ 考勤状态字典表（配合使用）

```sql
CREATE TABLE base_attendance_status (
  status_code VARCHAR(4) PRIMARY KEY COMMENT '状态编码',
  status_name VARCHAR(32) NOT NULL COMMENT '状态名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤状态字典表';

INSERT INTO base_attendance_status VALUES
('N', '正常'),
('L', '迟到'),
('E', '早退'),
('A', '缺勤');
```

---

# 三、典型查询示例（你以后一定会用）

### 1️⃣ 查询学生当天考勤

```sql
SELECT *
FROM att_record
WHERE student_code = 'STU20250001'
  AND attend_date = CURDATE();
```

---

### 2️⃣ 查询班级当天异常考勤

```sql
SELECT *
FROM att_record
WHERE class_code = 'CLS010203'
  AND attend_date = CURDATE()
  AND status_code <> 'N';
```

---

### 3️⃣ 查询学生当前所在班级

```sql
SELECT class_code
FROM stu_student_class
WHERE student_code = 'STU20250001'
  AND end_date IS NULL;
```

---

# 四、这套 SQL 的“隐形价值”

* ✅ 支撑真实学校规模
* ✅ 转班 / 补卡不炸
* ✅ 日后做分库分表也稳
* ✅ 面试官一看就知道你不是写 CRUD 的

---

下一步我建议你**进入真正的开发阶段**了，继续二选一：
1️⃣ **Spring Boot 实体类 + Repository 设计（直接对这些表）**
2️⃣ **“上学打卡”完整业务流程 + 代码骨架**

你选，我继续带你把系统跑起来。
