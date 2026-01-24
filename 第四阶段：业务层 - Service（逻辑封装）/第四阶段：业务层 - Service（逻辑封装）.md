很好，这一阶段是**把接口从“能跑”升级为“能管”**。
Controller 像前台，Service 像中控室，真正的判断、规则、兜底都藏在这里 🧠

---

# 第四阶段：业务层 / Service（逻辑封装）

## 🎯 阶段目标

完成后你应该能：

* 明确 **Controller 只接请求，不写判断**
* 用 Service 封装业务逻辑
* 看懂并编写：

  * 状态判断
  * 组合逻辑
  * 运维规则
* 为后续：

  * 数据库
  * 配置中心
  * 微服务调用
    打好“插槽”

---

## 一、为什么一定要有 Service？

### ❌ 错误姿势（新手常见）

```java
@GetMapping("/health")
public Result<String> health() {
    if (cpu > 80 && disk < 10) {
        return Result.error("DOWN");
    }
    return Result.success("UP");
}
```

问题：

* 判断逻辑写死在接口
* 改规则 = 改接口
* 运维无法复用

---

### ✅ 正确姿势（生产级）

```java
@GetMapping("/health")
public Result<String> health() {
    return healthService.health();
}
```

Controller **不关心为什么 UP / DOWN**
它只负责：

> 请求进来 → 结果出去

---

## 二、Service 的标准结构（必会）

### 1️⃣ 接口定义规则

```java
public interface HealthService {

    /**
     * 服务健康状态
     */
    Result<String> health();
}
```

📌 接口的意义：

* 定义“能干什么”
* 不关心“怎么干”

---

### 2️⃣ 实现类写逻辑

```java
@Service
public class HealthServiceImpl implements HealthService {

    @Override
    public Result<String> health() {
        boolean serviceOk = checkService();
        return serviceOk
                ? Result.success("UP")
                : Result.error("DOWN");
    }

    private boolean checkService() {
        // 未来这里可以是：
        // 1. 端口
        // 2. DB
        // 3. Redis
        // 4. 外部 HTTP
        return true;
    }
}
```

🧠 运维理解：

> Service = 规则仓库

---

## 三、Controller 注入 Service（依赖反转）

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
        return healthService.health();
    }
}
```

📌 关键点：

* Controller **不 new**
* Spring 负责组装
* 这是 IoC 的第一次实战

---

## 四、Service 里最常见的 5 种逻辑（运维向）

### 1️⃣ 状态判断

```java
return alive ? ok() : down();
```

### 2️⃣ 多条件组合

```java
if (!dbOk) return down("DB");
if (!redisOk) return down("REDIS");
return ok();
```

### 3️⃣ 阈值判断

```java
if (diskUsage > 90) {
    return Result.error("DISK FULL");
}
```

### 4️⃣ 兜底保护（非常重要）

```java
try {
    return check();
} catch (Exception e) {
    log.error("health error", e);
    return Result.error("UNKNOWN");
}
```

### 5️⃣ 降级逻辑（运维高级）

```java
if (timeout) {
    return Result.success("DEGRADED");
}
```

---

## 五、Service 和工具类的边界

### ❌ 错误

```java
@Service
public class HealthServiceImpl {
    public boolean ping(String host) {
        // 工具逻辑
    }
}
```

### ✅ 正确

```java
@Component
public class NetUtil {
    public boolean ping(String host) {}
}
```

```java
@Service
public class HealthServiceImpl {
    private final NetUtil netUtil;
}
```

📌 Service 只**组合规则**
工具类只**干活**

---

## 六、这一阶段你必须完成的练习

### ✅ 练习 1：拆判断

* Controller 只 1 行
* 所有 if 写进 Service

### ✅ 练习 2：模拟失败

* 随机返回 UP / DOWN
* 打日志

### ✅ 练习 3：错误兜底

* try-catch
* 返回统一 Result

---

## 七、这一阶段完成后，你的能力跃迁

你已经：

* 理解 **业务逻辑分层**
* 能读懂真实微服务 Service
* 知道：

  * 哪些逻辑该给开发
  * 哪些运维可以自己加
* 为下一步打下关键基础：

> **配置化 Service 行为**

---

## 下一阶段预告 🚀

**第五阶段：配置驱动的 Service（yml / profile / Nacos）**

* 不改代码切换环境
* 运维控制规则
* 健康检查配置化
* 灰度 / 降级开关

如果你愿意，我可以直接带你写一个：
👉 **运维可控的 HealthService 实战版**
