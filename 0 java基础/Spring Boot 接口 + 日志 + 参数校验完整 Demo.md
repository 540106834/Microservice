很好，这一段就是**运维看得懂、接口人能排错、面试能讲清楚的 Spring Boot 标准最小 Demo**。
我们会像搭积木一样来，不堆魔法，不绕路，每一块都知道自己在干嘛 

目标很明确：
**一个接口 = 参数校验 + 日志 + 统一返回 + 可排错**

---

## 一、项目结构（先看全景）

```
springboot-demo
├── src/main/java
│   └── com/example/demo
│       ├── DemoApplication.java
│       ├── controller
│       │   └── HealthController.java
│       ├── dto
│       │   └── Result.java
│       ├── util
│       │   └── LogUtil.java
│       └── service
│           └── HealthService.java
└── src/main/resources
    └── application.yml
```

---

## 二、启动类（不动它）

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

---

## 三、统一返回结构（接口骨架）

```java
package com.example.demo.dto;

public class Result {

    private int code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        Result r = new Result();
        r.code = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static Result error(String msg) {
        Result r = new Result();
        r.code = 400;
        r.message = msg;
        return r;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}
```

---

## 四、日志工具类（让你知道日志从哪来）

```java
package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
```

---

## 五、Service 层（业务在哪）

```java
package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public boolean isHealthy(int cpu) {
        return cpu < 80;
    }
}
```

---

## 六、Controller（重点）

```java
package com.example.demo.controller;

import com.example.demo.dto.Result;
import com.example.demo.service.HealthService;
import com.example.demo.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HealthController {

    private static final Logger log = LogUtil.getLogger(HealthController.class);
    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public Result health(
            @RequestParam(required = false) Integer cpu
    ) {
        log.info("health check request, cpu={}", cpu);

        if (cpu == null) {
            log.warn("cpu param missing");
            return Result.error("cpu param required");
        }

        boolean ok = healthService.isHealthy(cpu);

        if (!ok) {
            log.error("cpu overload: {}%", cpu);
            return Result.error("CPU overload");
        }

        return Result.success("OK");
    }
}
```

---

## 七、配置文件（日志级别）

```yaml
server:
  port: 8080

logging:
  level:
    root: INFO
    com.example.demo: INFO
```

---

## 八、运行 & 测试（运维视角）

### 启动

```bash
mvn spring-boot:run
```

### 正常请求

```bash
curl "http://localhost:8080/api/health?cpu=30"
```

返回：

```json
{ "code": 200, "message": "success", "data": "OK" }
```

### 参数缺失

```bash
curl "http://localhost:8080/api/health"
```

日志：

```
WARN cpu param missing
```

---

## 九、你现在已经掌握的能力

✔ Controller 在干嘛
✔ 日志什么时候打、打在哪
✔ 参数为什么要校验
✔ Service 层存在的意义
✔ 接口异常如何暴露

这些正是：

* **排线上接口问题**
* **看懂同事代码**
* **理解 Spring Cloud / Nacos / K8s 的地基**

---

## 十、下一步进阶（强烈推荐）

我可以继续直接带你做：

1. 🔥 **AOP 统一日志（真实生产写法）**
2. 🔥 **参数校验 @Valid + 全局异常处理**
3. 🔥 **Nacos 配置 + 动态刷新**
4. 🔥 **接口慢日志 + traceId**
5. 🔥 **K8s 探针就绪 / 存活接口设计**

你选 1 个，我就继续往下搭一层楼 🏗️
