这一阶段，是把你的接口**打磨成“能上生产线的零件”**。
不只是能跑，而是能被盯、被管、被限、被救 

---

# 第六阶段：Web / API 高级（运维实战层）

##  阶段目标（一句话版）

> **让接口在真实生产环境中“可监控、可保护、可回滚”**

完成后，你的服务已经具备：

* 运维可观测性
* 安全边界
* K8s 友好度

---

## 一、请求链路全景图（先有全局感）

```text
请求进来
 ↓
Filter（最外层）
 ↓
Interceptor（登录 / 权限）
 ↓
Controller
 ↓
Service
 ↓
Repository
 ↑
AOP（日志 / 耗时）
 ↑
异常处理（兜底）
```

记住一句口诀：

> **Filter 管请求，Interceptor 管人，AOP 管事**

---

## 二、Filter：最外层的“门禁”

### 用途（运维常用）

* 请求编码
* IP 白名单
* TraceId 注入

### 示例

```java
@Component
public class TraceFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest req,
            ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        long start = System.currentTimeMillis();
        chain.doFilter(req, res);
        long cost = System.currentTimeMillis() - start;

        log.info("request cost={}ms", cost);
    }
}
```

 特点：

* 不知道 Controller 是谁
* 但能看到所有请求

---

## 三、Interceptor：登录 / 权限检查

### 1️ 拦截器本体

```java
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest req,
            HttpServletResponse res,
            Object handler) {

        String token = req.getHeader("token");
        return token != null;
    }
}
```

### 2️ 注册

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/api/**");
    }
}
```

 拦截器特点：

* 知道是哪个接口
* 可以阻止 Controller 执行

---

## 四、AOP：不侵入业务的“外挂逻辑”

### 用途（运维超高频）

* 接口耗时
* 参数日志
* 异常捕获

### 示例：接口耗时统计

```java
@Aspect
@Component
public class TimeAspect {

    @Around("execution(* com.xxx.controller..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        log.info("cost={}ms", System.currentTimeMillis() - start);
        return result;
    }
}
```

 运维理解：

> AOP = 给所有接口装传感器

---

## 五、配置中心：行为从代码迁出

### 1️ yml 配置

```yaml
app:
  health:
    enabled: true
```

### 2️ 读取配置

```java
@Value("${app.health.enabled}")
private boolean healthEnabled;
```

### 3️ Service 中使用

```java
if (!healthEnabled) {
    return Result.error("DISABLED");
}
```

📌 运维意义：

* 不改代码
* 不重新编译
* 行为可控

---

## 六、健康检查 & 探针（K8s 必会）

### 1️ Liveness（活着没）

```java
@GetMapping("/health")
public String health() {
    return "OK";
}
```

### 2️ Readiness（能接活没）

```java
@GetMapping("/ready")
public String ready() {
    return dbOk ? "READY" : "NOT_READY";
}
```

📌 运维经验：

* health 快
* ready 严格

---

## 七、统一异常处理（生产救命技能）

### 1️ 异常处理器

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handle(Exception e) {
        log.error("error", e);
        return Result.error("SYSTEM_ERROR");
    }
}
```

📌 好处：

* 接口不炸
* 错误格式统一
* 监控好接

---

## 八、这一阶段你必须完成的练习

### ✅ 练习 1：接口耗时日志

* AOP
* 输出 path + cost

### ✅ 练习 2：鉴权拦截

* header 校验
* 未登录直接返回

### ✅ 练习 3：K8s 探针

* health / ready 分离

---

## 九、完成这一阶段后的真实战力

你已经能：

* 看懂完整请求链路
* 排查：

  * 为什么接口慢
  * 为什么被拦
  * 为什么 K8s 重启 Pod
* 自己补：

  * 运维接口
  * 监控钩子
  * 限流前置点

---

## 下一阶段预告 

**第七阶段：微服务 & 云原生整合**

* Nacos
* Gateway
* Docker / K8s
* 日志 / 监控 / 链路追踪

到这一步，你已经不是“会 Java 的运维”，而是

> **能读、能改、能护住微服务的云原生运维**
