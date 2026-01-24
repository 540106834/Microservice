好，进入 **Web 第 2 步** 🚪
这一关开始，你写的代码已经**像一个真正的接口服务**了。

目标只有一句话：

> **接口能收参数 + 返回统一 JSON + 运维一眼能看懂**

---

## 一、为什么要“统一返回 JSON”

先看“野生接口”的样子 👀

```json
"ok"
```

```json
{ "name": "Tom" }
```

```json
true
```

运维和前端的内心独白：
“这是啥结构？成功还是失败？状态码靠猜？”

---

### ✅ 标准 Web 返回结构（Result）

```json
{
  "code": 200,
  "message": "success",
  "data": {...}
}
```

这就是 **接口世界的普通话**。

---

## 二、Result 类（核心骨架）

### 1️⃣ 最推荐写法（泛型）

```java
public class Result<T> {

    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    // getter / setter 可省略（IDE 生成）
}
```

🧠 记忆点：

* `T` = 返回数据的类型
* `Result<User>`、`Result<List<User>>` 都合法
* Controller 永远返回 `Result`

---

## 三、请求参数（Web 的三种入口）

### 1️⃣ URL 参数（@RequestParam）

```http
GET /user?id=1
```

```java
@GetMapping("/user")
public Result<String> getUser(@RequestParam int id) {
    return Result.success("userId = " + id);
}
```

📌 特点：

* 适合简单参数
* 运维 curl / 浏览器直接测

---

### 2️⃣ 路径参数（@PathVariable）

```http
GET /user/1
```

```java
@GetMapping("/user/{id}")
public Result<String> getUser(@PathVariable int id) {
    return Result.success("userId = " + id);
}
```

📌 REST 风格接口最常见。

---

### 3️⃣ JSON 参数（@RequestBody）🔥

```http
POST /user
Content-Type: application/json
```

```json
{
  "name": "Tom",
  "age": 18
}
```

```java
public class UserDTO {
    public String name;
    public int age;
}
```

```java
@PostMapping("/user")
public Result<UserDTO> create(@RequestBody UserDTO user) {
    return Result.success(user);
}
```

🧠 Spring 自动做的事：

* JSON → Java 对象
* 字段名自动映射

---

## 四、Controller 标准模板（背这个）

```java
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("ok");
    }

    @GetMapping("/user/{id}")
    public Result<Integer> getUser(@PathVariable int id) {
        return Result.success(id);
    }

    @PostMapping("/user")
    public Result<Object> create(@RequestBody Object body) {
        return Result.success(body);
    }
}
```

🎯 你以后 80% Controller 都是这个结构。

---

## 五、错误返回（运维视角很重要）

```java
@GetMapping("/fail")
public Result<Object> fail() {
    return Result.error(500, "service unavailable");
}
```

返回：

```json
{
  "code": 500,
  "message": "service unavailable",
  "data": null
}
```

📌 运维最关心：

* code
* message
* 是否稳定

---

## 六、curl 快速验证（运维必会）

```bash
curl http://localhost:8080/api/health
```

```bash
curl http://localhost:8080/api/user/1
```

```bash
curl -X POST http://localhost:8080/api/user \
  -H "Content-Type: application/json" \
  -d '{"name":"Tom","age":18}'
```

---

## 七、这一关你算通关了如果你能：

✅ 看懂 `Result<T>`
✅ 知道什么时候用 `@RequestParam / @PathVariable / @RequestBody`
✅ Controller 只干一件事：**收参数 → 调 Service → 包 Result**

---

## 下一步怎么走（强烈建议）

我给你三条进阶路线，你选一条继续走：

1️⃣ **Web 第 3 步：参数校验（@Valid）+ 错误统一处理**
2️⃣ **接口日志规范（请求 / 响应 / traceId）运维视角**
3️⃣ **Service 分层 + Mock 思维（接口解耦真正用法）**

你点哪一条，我直接往下铺路 🚀
