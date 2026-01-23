好，我们来做一套**可直接上手敲、覆盖 Java 基础语法的练习案例合集**。它们像一组练功木人桩，每一根都在敲同一件事：让你对语法产生肌肉记忆，而不是背口诀 

我会按**由浅入深**来，每个案例都给：

* 练习目标
* 示例代码（可直接编译运行）
* 你可以改动的点

---

## 一、变量 + 输出（起手式）

### 目标

* 变量声明
* 基本类型
* `System.out.println`

### 代码

```java
public class Demo01 {
    public static void main(String[] args) {
        int age = 28;
        double salary = 8500.50;
        boolean isAdmin = true;
        String name = "ZhangSan";

        System.out.println("name = " + name);
        System.out.println("age = " + age);
        System.out.println("salary = " + salary);
        System.out.println("isAdmin = " + isAdmin);
    }
}
```

### 练习建议

* 把 `int` 换成 `long`
* 把 `+` 拼接改成自己想要的输出格式

---

## 二、if / else 判断（条件反射）

### 目标

* 条件判断
* 比较运算符

```java
public class Demo02 {
    public static void main(String[] args) {
        int score = 72;

        if (score >= 90) {
            System.out.println("优秀");
        } else if (score >= 60) {
            System.out.println("及格");
        } else {
            System.out.println("不及格");
        }
    }
}
```

### 练习建议

* 改成判断“是否是运维值班人员”
* 多加一个区间判断

---

## 三、for 循环（节奏感）

### 目标

* for 循环
* 累加

```java
public class Demo01 {
    public static void main(String[] args) {
        int sum = 0;

        for (int i = 1; i <= 100; i++) {
            sum += i;
        }

        System.out.println("sum = " + sum);
    }
}
```

### 练习建议

* 改成只加偶数
* 把 `100` 改成变量

---

## 四、while + break（循环控制）

```java
public class Demo04 {
    public static void main(String[] args) {
        int i = 1;

        while (true) {
            if (i > 5) {
                break;
            }
            System.out.println("i = " + i);
            i++;
        }
    }
}
```

### 练习建议

* 用 `continue`
* 改成模拟重试 3 次

---

## 五、数组（最常用数据结构）

### 目标

* 数组定义
* 下标访问
* 遍历

```java
public class Demo05 {
    public static void main(String[] args) {
        int[] nums = {10, 20, 30, 40};

        for (int i = 0; i < nums.length; i++) {
            System.out.println(nums[i]);
        }
    }
}
```

### 练习建议

* 求最大值
* 求平均值

---

## 六、方法（Java 世界的积木）

### 目标

* 方法定义
* 参数
* 返回值

```java
public class Demo01 {

    public static int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        int result = add(3, 5);
        System.out.println(result);
    }
}
```

### 练习建议

* 写一个 `isEven(int n)`
* 写一个 `max(int a, int b)`

---

## 七、类 + 对象（真正进入 Java）

```java
class User {
    String name;
    int age;

    void sayHello() {
        System.out.println("Hello, I am " + name);
    }
}

public class Demo01 {
    public static void main(String[] args) {
        User u = new User();
        u.name = "LiSi";
        u.age = 20;

        u.sayHello();
    }
}
```

### 练习建议

* 给 `User` 加构造方法
* 把属性设为 `private`

---

## 八、综合小案例（运维味道）

### 目标

* if + 方法 + 数组 + 循环

```java
public class Demo01 {

    static boolean checkServer(int cpu) {
        return cpu < 80;
    }

    public static void main(String[] args) {
        int[] servers = {45, 66, 90, 30};

        for (int cpu : servers) {
            if (checkServer(cpu)) {
                System.out.println(cpu + "% OK");
            } else {
                System.out.println(cpu + "% ALERT");
            }
        }
    }
}
```

---

## 建议的练习节奏（很重要）

* 每天 **3–5 个 Demo**
* 必须：**手敲 + 改代码 + 看报错**
* 不要背语法，靠“写错”记住

---

如果你愿意，我可以下一步直接给你：

* 🔹 **50 道 Java 基础练习题（带答案）**
* 🔹 **运维/接口方向专用 Java 练习**
* 🔹 **把这些 Demo 串成一个小项目**

你选一个方向，我继续陪你练。

好，这一组我们就**专门为运维 + 接口方向定制 Java 练习**。
不讲花架子，每一题都像你将来会在日志、接口、配置、健康检查里真正遇到的场景。代码是工具，目标是“看懂、改得动、不慌” 🛠️📡

我按**真实使用频率**来排，从最常见到稍微进阶。

---

## 练习 1：健康检查接口（接口人必做）

### 场景

服务是否存活，k8s / SLB / Nginx 都会打这个接口。

```java
public class HealthCheck {

    public static String health() {
        return "OK";
    }

    public static void main(String[] args) {
        System.out.println(health());
    }
}
```

### 练习目标

* 方法返回值
* 接口返回字符串

### 可改练习

* 返回 `"UP"` / `"DOWN"`
* 增加时间戳

---

## 练习 2：端口状态模拟（if 判断）

```java
public class Demo {

    static boolean isOpen(int port) {
        if (port == 80 || port == 443) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        int port = 8080;

        if (isOpen(port)) {
            System.out.println("Port " + port + " is OPEN");
        } else {
            System.out.println("Port " + port + " is CLOSED");
        }
    }
}
```

### 运维对应

* 防火墙
* 安全组
* 端口探活

---

## 练习 3：CPU 使用率告警（数组 + 循环）

```java
public class CpuMonitor {

    public static void main(String[] args) {
        int[] cpuUsage = {30, 45, 90, 70, 88};

        for (int cpu : cpuUsage) {
            if (cpu >= 85) {
                System.out.println(cpu + "% ALERT");
            } else {
                System.out.println(cpu + "% OK");
            }
        }
    }
}
```

### 运维影子

* Prometheus
* 告警阈值

---

## 练习 4：接口参数校验（非常重要）

```java
public class Demo01 {

    static boolean valid(String username, String token) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (token == null || token.length() < 10) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(valid("admin", "1234567890"));
        System.out.println(valid("", "123"));
    }
}
```

### 运维 / API

* 网关参数校验
* 防止无效请求

---

## 练习 5：返回统一接口结果（真实 API 风格）

```java
class Result {
    int code;
    String message;
    Object data;
}

public class Demo01 {

    static Result success(Object data) {
        Result r = new Result();
        r.code = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    static Result error(String msg) {
        Result r = new Result();
        r.code = 500;
        r.message = msg;
        return r;
    }

    public static void main(String[] args) {
        System.out.println(success("ok").code);
        System.out.println(error("fail").message);
    }
}


```

### 你未来会看到的

```json
{ "code": 200, "message": "success", "data": "ok" }
```

---

## 练习 6：日志打印（接口排错第一步）

```java
public class LoggerDemo {

    static void log(String level, String msg) {
        System.out.println("[" + level + "] " + msg);
    }

    public static void main(String[] args) {
        log("INFO", "service start");
        log("ERROR", "db connection failed");
    }
}
```

### 运维对应

* ELK
* 日志级别筛选

---

## 练习 7：模拟配置读取（配置中心影子）

```java
public class Demo01 {

    static String getConfig(String key) {
        if ("db.host".equals(key)) {
            return "127.0.0.1";
        }
        if ("db.port".equals(key)) {
            return "3306";
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getConfig("db.host"));
    }
}
```

---

## 练习 8：接口耗时统计（慢接口排查）

```java
public class Demo01 {

    static void api() {
        try {
            Thread.sleep(300);
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        api();
        long end = System.currentTimeMillis();

        System.out.println("cost = " + (end - start) + " ms");
    }
}
```

---

## 运维 Java 学习正确路径（重点）

你不需要学成 Java 架构师，只要会这几件事：

| 能力            | 是否必会 |
| ------------- | ---- |
| 看懂 Controller | ✅    |
| 看日志           | ✅    |
| 改配置           | ✅    |
| 判断接口返回        | ✅    |
| 写简单工具         | ✅    |
| JVM 深入        | ❌    |

---

## 下一步我可以继续给你

*  **Spring Boot 接口 + 日志 + 参数校验完整 Demo**
*  **Nacos + Java 配置读取真实案例**
*  **K8s 环境下 Java 服务是怎么启动和挂的**
*  **10 个运维面试常问 Java 问题**

你挑一个方向，我继续往下铺路 

