# **POM.xml 就是 Spring Boot 项目的总控台**
像一张总电路图，决定了你这台应用机器能接什么电、跑多快、装哪些模块。

我按**运维 / 接口型 Spring Boot 项目**的视角，把 `pom.xml` 从上到下拆给你看，不讲 Maven 教条，只讲“你以后改它时心里要有数”。

---

## 一、POM.xml 是干嘛的（一句话版）

> **POM.xml = 项目的依赖说明书 + 构建规则 + 版本控制中心**

你平时做的这些事，几乎都绕不开它：

* 引入 Spring Boot / Web / MySQL / Redis
* 控制依赖版本冲突
* 打 jar 包、Docker 镜像
* 区分 dev / test / prod 环境
* 统一 Java 版本

---

## 二、一个“标准 Spring Boot POM”骨架（先整体看）

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>user-service</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>user-service</name>
    <description>User Service Demo</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
    </parent>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- 依赖写在这里 -->
    </dependencies>

    <build>
        <!-- 构建规则 -->
    </build>

</project>
```

下面我们逐块拆。

---

## 三、项目信息区（是谁）

### 1️⃣ `<groupId>`

```xml
<groupId>com.example</groupId>
```

**公司 / 组织名空间**

* 通常是反域名
* 运维意义：
  👉 **区分不同系统、不同公司产物**

常见：

* `com.company.project`
* `cn.xxx.ops`
* `org.xxx.platform`

---

### 2️⃣ `<artifactId>`

```xml
<artifactId>user-service</artifactId>
```

**这个项目叫啥**

* 最终 jar 名：`user-service-1.0.0.jar`
* 在依赖中唯一定位它

---

### 3️⃣ `<version>`

```xml
<version>1.0.0</version>
```

**版本号**

运维建议：

* 开发阶段：`1.0.0-SNAPSHOT`
* 发布包：`1.0.0`

---

### 4️⃣ `<packaging>`

```xml
<packaging>jar</packaging>
```

* Spring Boot 基本都是 `jar`
* 老 Java Web 才是 `war`

---

## 四、parent：Spring Boot 的“总管家”

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.1</version>
</parent>
```

### 这行的真实作用是啥？

> **帮你统一依赖版本，避免地狱级冲突**

你不用再写：

* spring-web 版本
* jackson 版本
* logback 版本
* tomcat 版本

Spring Boot 已经帮你配好一套“能一起活”的版本组合。

🚨 **运维常识**

* **升级 Spring Boot = 一次系统级升级**
* 不能随便改版本

---

## 五、properties：全局变量区（非常重要）

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

### 用来干嘛？

* 统一 Java 版本
* 统一第三方库版本
* 避免版本散落满天

示例：

```xml
<properties>
    <java.version>17</java.version>
    <mysql.version>8.0.33</mysql.version>
</properties>
```

然后在依赖中用：

```xml
<version>${mysql.version}</version>
```

🧠 **运维视角**
👉 出问题时，你只改一行就能整体切换版本。

---

## 六、dependencies：项目“能干啥”的核心区

### 1️⃣ Web 服务能力

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

带来了：

* Controller
* 内嵌 Tomcat
* JSON 转换（Jackson）
* REST API 能力

---

### 2️⃣ 日志（默认就有）

```xml
spring-boot-starter-logging
```

你没写，但它存在。

所以你能直接用：

```java
private static final Logger log =
    LoggerFactory.getLogger(Xxx.class);
```

---

### 3️⃣ 数据库（示例）

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

* 只是“能连数据库”
* 真正配置在 `application.yml`

---

### 4️⃣ 测试依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

`scope=test` 的含义：

* 运行时不打进 jar
* 只在测试阶段存在

---

## 七、build：打包 & 插件区（运维必看）

### Spring Boot 打包插件

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

作用：

* 把所有依赖打进一个 jar
* 支持 `java -jar xxx.jar`
* 生成 **可直接部署的包**

没有它：

* jar 不能直接跑
* 运维会骂人 😄

---

## 八、运维最常踩的 POM 坑 ⚠️

### ❌ 手动指定 starter 里的版本

```xml
<version>5.3.20</version> ❌
```

**后果：**

* 依赖冲突
* 启动异常
* 类找不到

---

### ❌ 引入重复 starter

```xml
spring-boot-starter-web
spring-boot-starter-webflux
```

除非你非常清楚在干嘛，否则别混。

---

## 九、你现在这个阶段，POM 只需要记住这 5 点

1. **parent 决定一切基础版本**
2. **dependencies 决定功能**
3. **properties 是版本控制核心**
4. **spring-boot-maven-plugin 必须有**
5. **不乱加 version**

---

## 十、下一步我可以帮你做什么？

你可以直接说：

* 「给我一份**运维 / API 最小 POM**」
* 「Nacos + Spring Boot 的 POM 怎么写」
* 「POM 里 profile 是干嘛的」
* 「依赖冲突怎么排」

你已经站在 **“能看懂项目结构”的门口** 了，接下来就是越走越稳。🧱🚀
