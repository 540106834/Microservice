太好了，这一题正适合**运维 / API 实战**。
我们不玩抽象概念，直接来一套**像真实线上接口一样的练习**。
目标只有一个：
👉 **你一看返回 JSON，就知道接口写对没写对**。

---

# 🧪 实战题：运维 / API 专用

## Result + List + Map 综合练习

---

## 🎯 业务背景（非常真实）

你在运维平台里，要提供一个接口：

> **查询服务器状态列表**

每台服务器需要返回：

* 主机名
* IP
* CPU 使用率
* 内存使用率
* 当前状态（UP / DOWN）

接口返回要求：

* 统一 `Result<T>` 结构
* `data` 是 **List**
* List 里的每一项是 **Map**

---

## 一、返回结构目标（先看终点）

接口最终返回 JSON 结构应类似：

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "host": "server-01",
      "ip": "10.0.0.1",
      "cpu": 0.35,
      "memory": 0.62,
      "status": "UP"
    },
    {
      "host": "server-02",
      "ip": "10.0.0.2",
      "cpu": 0.92,
      "memory": 0.88,
      "status": "DOWN"
    }
  ]
}
```

记住一句话：

> **Result 包 List，List 装 Map**

---

## 二、你需要完成的类结构

```
controller/
 └── ServerController.java

service/
 ├── ServerService.java
 └── ServerServiceImpl.java

common/
 └── Result.java
```

---

## 三、Result<T>（你可以直接用）

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

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    public T getData() {
        return data;
    }
}
```

---

## 四、Service 接口（你要写）

👉 **思考点**：

* 返回类型是什么？
* 要不要泛型？

✍️ 你应该写成这样：

```java
public interface ServerService {

    Result<List<Map<String, Object>>> listServers();

}
```

---

## 五、Service 实现类（重点）

```java
@Service
public class ServerServiceImpl implements ServerService {

    @Override
    public Result<List<Map<String, Object>>> listServers() {

        List<Map<String, Object>> servers = new ArrayList<>();

        Map<String, Object> server1 = new HashMap<>();
        server1.put("host", "server-01");
        server1.put("ip", "10.0.0.1");
        server1.put("cpu", 0.35);
        server1.put("memory", 0.62);
        server1.put("status", "UP");

        Map<String, Object> server2 = new HashMap<>();
        server2.put("host", "server-02");
        server2.put("ip", "10.0.0.2");
        server2.put("cpu", 0.92);
        server2.put("memory", 0.88);
        server2.put("status", "DOWN");

        servers.add(server1);
        servers.add(server2);

        return Result.success(servers);
    }
}
```

📌 **这里你一定要看懂**：

* `List<Map<String, Object>>`
* 每一台服务器是一个 Map
* Map 里是运维最常见的键值数据

---

## 六、Controller（接口层）

```java
@RestController
@RequestMapping("/api/servers")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping
    public Result<List<Map<String, Object>>> list() {
        return serverService.listServers();
    }
}
```

📌 Controller 特点：

* 不关心数据怎么来的
* 不处理业务
* 直接返回 Result

---

## 七、你现在应该真正“理解”的 5 件事

1️⃣ **为什么不用 Object 乱装**

* 泛型让接口返回值可控、可读、安全

2️⃣ **为什么 data 是 List**

* 接口查询通常返回多条数据

3️⃣ **为什么 List 里是 Map**

* 运维数据字段灵活
* 不想为每个场景建 DTO

4️⃣ **为什么要 Result**

* 前端 / 网关 / 监控统一解析

5️⃣ **为什么 Service 返回 Result**

* Controller 变薄
* 错误处理可统一

---

## 八、升级挑战（非常推荐）

你可以尝试自己改造：

### 🔹 Challenge 1

如果 CPU > 0.8 → status 自动变成 `DOWN`

### 🔹 Challenge 2

增加一个接口：

```http
GET /api/servers/summary
```

返回：

```json
{
  "total": 2,
  "up": 1,
  "down": 1
}
```

返回类型应是：

```java
Result<Map<String, Integer>>
```

---

如果你愿意，下一步我可以：

* 帮你 **逐行检查你自己写的版本**
* 或者升级到 **K8s / Pod / 节点状态接口实战**
* 或者加入 **异常 + 日志 + 告警版**

你选哪一个，我就带你继续往真实生产环境走。
