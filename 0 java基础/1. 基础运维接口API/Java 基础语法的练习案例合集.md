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

