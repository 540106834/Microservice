好，**第三阶段**是真正从“会 Java”走进“能跑接口”的关口，也是运维视角里**最有成就感的一段**。
这里不是写花活，而是搭骨架，插电源，一启动就能对外说话 📡

---

## 第三阶段：Spring Boot 入门

**接口搭建 · 启动即服务 · 返回 JSON**

### 🎯 阶段目标（非常清晰）

完成这一阶段，你应该能做到：

* 用 `main` 启动一个 **Web 服务**
* 浏览器 / curl 访问接口，拿到 **JSON**
* 看懂 Controller / Service / Result 的分层
* 明白：
  👉 **为什么 main 一启动，Controller 就能被访问**

---

## 一、Spring Boot 核心认知（先立世界观）

### 1️⃣ Spring Boot 是什么（运维版理解）

> Spring Boot =
> **Java Web 应用的“自带电源 + 自动接线 + 默认配置”**

它帮你做了三件事：

1. 内嵌 Tomcat（不用装服务器）
2. 自动扫描类（不用手动 new）
3. 自动返回 JSON（不用自己拼字符串）

---

### 2️⃣ 最重要的一个类

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

你要记住一句话：

> **Spring Boot 的 main = 服务启动入口，不是业务入口**

---

## 二、必会目录结构（运维接口方向）

```text
com.example.demo
├── Application.java        # 启动类（根）
├── controller
│   └── HealthController.java
├── service
│   └── HealthService.java
│   └── impl
│       └── HealthServiceImpl.java
├── dto
│   └── Result.java
```

🧠 运维视角理解：

* controller：对外 API 门口
* service：业务判断
* dto：接口返回结构
* Application：**扫描这些东西的探照灯**

---

## 三、Controller：接口从这里“冒头”

### 1️⃣ 最小可用 Controller

```java
@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("UP");
    }
}
```

访问：

```bash
curl http://localhost:8080/api/health
```

返回：

```json
{
  "code": 200,
  "message": "success",
  "data": "UP"
}
```

📌 **关键注解只记 3 个**

| 注解                | 作用      |
| ----------------- | ------- |
| `@RestController` | 返回 JSON |
| `@RequestMapping` | 路径前缀    |
| `@GetMapping`     | GET 接口  |

---

## 四、Result：为什么自动变成 JSON？

```java
public class Result<T> {
    private int code;
    private String message;
    private T data;

    // 构造 + getter/setter
}
```

### 核心原理（不用死记）

> Spring Boot 启动时：
>
> * 发现返回的是对象
> * 使用 Jackson
> * 自动序列化成 JSON

📌 **你不用写 JSON，Spring 帮你翻译**

---

## 五、Service：把“判断”从 Controller 拆出去

### 1️⃣ Service 接口

```java
public interface HealthService {
    boolean check();
}
```

### 2️⃣ 实现类

```java
@Service
public class HealthServiceImpl implements HealthService {

    @Override
    public boolean check() {
        return true;
    }
}
```

### 3️⃣ Controller 注入 Service

```java
@RestController
@RequestMapping("/api")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public Result<String> health() {
        return healthService.check()
                ? Result.success("UP")
                : Result.error("DOWN");
    }
}
```

🧠 运维理解：

* Controller 不判断状态
* Service 决定 UP / DOWN
* 未来可以接：

  * 数据库
  * Redis
  * shell 命令
  * k8s API

---

## 六、main 启动后，为什么 Controller 能被加载？

这是**第三阶段最关键的理解点** 👇

### 启动流程一句话版：

```
main 启动
 → Spring Boot 扫描包
   → 找到 @RestController
   → 注册 URL 映射
   → Tomcat 监听端口
```

### 扫描范围由谁决定？

```java
@SpringBootApplication
```

它默认扫描：

> **启动类所在包 + 子包**

📌 所以运维常见坑是：

> Controller 不在启动类包下面
> 👉 接口 404

---

## 七、这一阶段你必须亲手完成的练习

### ✅ 练习 1：健康检查接口

* `/health` 返回 UP / DOWN
* 用三元运算符

### ✅ 练习 2：参数接口

```java
@GetMapping("/echo")
public Result<String> echo(@RequestParam String msg)
```

### ✅ 练习 3：日志输出

```java
log.info("health check called");
```

---

## 八、完成第三阶段，你在运维体系中的位置

你已经可以：

* 看懂 Spring Boot 微服务
* 明白接口从哪里来
* 排查：

  * 端口不通
  * 接口 404
  * Bean 注入失败
* 和开发说同一种语言 🧠

---

如果你愿意，下一步我可以直接给你：

* **第四阶段：Spring Boot + 运维实战**

  * profile / yml
  * 日志
  * 健康检查
  * 打包 jar
  * systemd / Docker / K8s 启动

你现在，已经站在“云原生运维能理解代码”的门口了 🚪
