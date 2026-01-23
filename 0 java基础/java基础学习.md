**Java 基础学习，面向 Web / API 开发**。
不是学院派，不是刷语法点，而是 **“能写接口、能上线、运维也看得懂”** 的那种。

我给你一套 **Web / API 向的 Java 基础主干**，每一块都和真实 API 直接挂钩。

---

## 一、Web / API 开发里的 Java 在干什么？

一句话版本：

> Java 负责接请求 → 执行业务 → 返回 JSON

放到 Spring Boot 里就是：

```
HTTP 请求
↓
Controller
↓
Service
↓
Repository（可选）
↓
返回 JSON
```

你学 Java 的目的，就是 **支撑这条链路**。

---

## 二、API 开发必学的 Java 基础模块（删繁就简）

### ① Java 基础语法（够用即可）

你至少要能无障碍看懂这些：

```java
int a = 10;

if (a > 0) {
    System.out.println("ok");
}

for (int i = 0; i < 3; i++) {
    ...
}
```

 Web 开发里几乎 **不写复杂算法**

---

### ② 类 & 对象（API 的骨架）

一个 API 本质是一堆类在配合。

```java
public class User {
    private Long id;
    private String name;
}
```

这是：

* 请求参数
* 返回对象
* 数据库实体

 你要知道：

* 成员变量 = 字段
* 方法 = 行为
* 一个 `.java` 文件 ≈ 一个角色

---

### ③ OOP 的核心：多态（Spring 灵魂）

```java
public interface UserService {
    User getById(Long id);
}
```

```java
@Service
public class UserServiceImpl implements UserService {
    public User getById(Long id) {
        ...
    }
}
```

Controller 永远 **依赖接口，不关心实现**。

 这就是：

* 解耦
* 可替换
* 单元测试友好

---

## 三、Web API 开发最核心的 5 个 Java 知识点

### 1 注解（你天天在用）

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        ...
    }
}
```

你至少要懂：

| 注解              | 作用        |
| --------------- | --------- |
| @RestController | 返回 JSON   |
| @RequestMapping | 路径        |
| @GetMapping     | GET       |
| @PostMapping    | POST      |
| @RequestBody    | JSON → 对象 |

---

### 2 JSON ↔ Java 对象（API 成败点）

```java
public class UserDTO {
    private String name;
    private Integer age;
}
```

```json
{
  "name": "Tom",
  "age": 18
}
```

 核心规则：

* 字段名一致
* 有 getter/setter
* 有无参构造

---

### 3 集合（返回列表的根）

```java
List<User> users = new ArrayList<>();
Map<Long, User> userMap = new HashMap<>();
```

Web 开发 80%：

* `List`
* `Map`

---

### 4 异常（API 稳定性）

```java
if (user == null) {
    throw new RuntimeException("user not found");
}
```

配合：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        ...
    }
}
```

👉 API 不应该把堆栈甩给前端。

---

### 5 日志（API 的黑匣子）

```java
log.info("get user id={}", id);
log.error("get user failed", e);
```

 Web API 日志三件套：

* 请求参数
* 错误堆栈
* 执行结果

---

## 四、一个最小可运行 API（完整）

```java
@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "hello");
        return res;
    }
}
```

访问：

```
GET http://localhost:8080/api/hello
```

返回：

```json
{"msg":"hello"}
```

 这就是完整 API 生命周期。

---

## 五、Web API 开发中 Java 新手常踩的坑（运维视角）

❌ Controller 写业务逻辑
❌ 成员变量当缓存
❌ HashMap 当数据库
❌ try-catch 吞异常
❌ 日志 println

这些都是 **线上事故种子** 

---

## 六、推荐你的学习顺序（Web 专用）

### 第 1 步

* 类 / 对象
* 接口 / 实现
* 注解

### 第 2 步

* JSON ↔ Java
* 集合
* 异常

### 第 3 步

* 日志
* 配置文件
* 简单 JVM 参数

---

## 七、下一步我可以直接带你实战

你可以直接选：

1️⃣ 从 0 写一个 **标准 REST API 项目结构**
2️⃣ 请求参数校验（@Valid）
3️⃣ API 返回统一结构设计
4️⃣ 接口日志 + traceId
5️⃣ Web API 常见 10 个生产坑

选一个，我直接开写，边写边讲 
