完美，这一步就是把 **设备 → Spring Boot → 数据库** 打通，让数据流走起来。

我帮你写一个 **最小可跑 Controller**，接收 JSON 数据，存到 `attendance_raw` 表。

---

# 1️ 创建 Controller `AttendanceRawController.java`

```java
package com.jsy.attendance.controller;

import com.jsy.attendance.entity.AttendanceRaw;
import com.jsy.attendance.repository.AttendanceRawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceRawController {

    @Autowired
    private AttendanceRawRepository attendanceRawRepository;

    /**
     * 接收原始打卡数据
     * 示例 POST JSON:
     * {
     *   "deviceSn":"860123456789012",
     *   "cardNo":"00004567",
     *   "eventTime":"2026-02-01T07:59:30",
     *   "eventType":0,
     *   "sn":"0001"
     * }
     */
    @PostMapping("/raw")
    public String receiveRawAttendance(@RequestBody AttendanceRaw raw) {
        // 如果未传 receivedAt，自动补上
        if (raw.getReceivedAt() == null) {
            raw.setReceivedAt(LocalDateTime.now());
        }
        // 默认未解析
        raw.setParsed(0);

        attendanceRawRepository.save(raw);
        return "ok";
    }
}
```

---

# 2️ 使用说明

* **请求 URL**：`POST http://localhost:8080/api/attendance/raw`
* **请求 Body（JSON）**：

```json
{
  "deviceSn":"860123456789012",
  "cardNo":"00004567",
  "eventTime":"2026-02-01T07:59:30",
  "eventType":0,
  "sn":"0001"
}
```

* **返回**：`"ok"` 表示存储成功

> ✅ 最小可跑闭环：设备数据 → Controller → Repository → `attendance_raw` 表

---

# 3️ 核心逻辑说明

1. **`@RestController` + `@RequestBody`**

   * 自动把 JSON 转成 `AttendanceRaw` 实体

2. **默认值处理**

   * `parsed` 默认 0
   * `receivedAt` 若未传，自动补当前时间

3. **数据库保存**

   * `attendanceRawRepository.save(raw)` → 插入 MySQL

4. **幂等性注意**

   * 数据库唯一约束 `(device_sn, sn)` 保证重复上传不会导致重复数据

---

# 4️ 下一步建议

现在系统可以：

1. 启动 Spring Boot
2. 接收设备上传的原始打卡数据
3. 自动写入 `attendance_raw` 表

下一步可以做：

* **Step 5：写 parser Service**

  * 把 `attendance_raw` 解析成 `att_record`
  * 判断迟到/早退状态
  * 写入业务表

这样就可以实现完整的**上学/放学打卡闭环**。

---

如果你愿意，我可以帮你直接写**最小可跑解析 Service + att_record 实体**，让打卡数据真正能变成考勤记录。

你希望我直接写吗？
