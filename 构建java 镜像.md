你现在的材料很简单：

```
基础镜像：eclipse-temurin:17-jre
应用：attend.jar
```

这就像已经有了发动机和飞船，只差最后一次组装 。下面给你一份**可直接投入生产的极简 Dockerfile**。

---

# ✅ 最推荐写法（80%公司这么用）

创建一个文件：

```
Dockerfile
```

内容：

```dockerfile
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY attend.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
```

就是这么短。没有多余层，没有花活。

---

# ✅ 开始构建

在 `Dockerfile` 和 `attend.jar` 同目录执行：

```bash
docker build -t attend:1.0 .
```

看一眼镜像：

```bash
docker images
```

---

# ✅ 启动容器测试

```bash
docker run -d -p 8080:8080 attend:1.0
```

如果是 Spring Boot：

打开：

```
http://服务器IP:8080
```

服务响应 = 构建成功 ✅

---

# ⭐ 强烈建议加这一版（更稳）

Java 在容器里有个经典误判：

> “我以为自己拥有整台服务器的内存。”

建议改成：

```dockerfile
ENTRYPOINT ["java",
"-XX:MaxRAMPercentage=75",
"-XX:+UseContainerSupport",
"-jar",
"/app/app.jar"]
```

效果：

* 避免 OOMKilled
* JVM 更听话
* 内存利用更健康

---

# 🔥 再升级一点点（企业味更浓）

## ✅ 创建非 root 用户（非常推荐）

很多安全扫描会检查这一条。

```dockerfile
FROM eclipse-temurin:17-jre

RUN useradd -m appuser

WORKDIR /app

COPY attend.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","/app/app.jar"]
```

好处：

✅ 降低容器逃逸风险
✅ 安全评分更高
✅ 更接近生产规范

---

# 🚨 一个常见大坑

## ❗ 不要这样：

```dockerfile
COPY . .
```

原因：

可能把这些也复制进去：

* `.git`
* 日志
* node_modules
* target 历史文件

镜像直接胖三圈。

👉 永远只 COPY **jar**。

---

# ⭐ 再给你一个高手小技巧

创建 `.dockerignore`：

```
.git
target/
*.log
node_modules
```

构建速度会明显提升。

---

# 🔥 如果你追求“更小镜像”

可以考虑：

```
eclipse-temurin:17-jre-alpine
```

但提醒一句：

👉 Alpine 使用 **musl libc**

有些 Java 库可能：

* 字体异常
* DNS 问题
* JNI 不兼容

稳定优先的话：

> **jammy / debian 系更稳。**

---

如果你愿意继续升级，我还能带你进入一个很多人后知后觉的领域：

## 👉 为什么同样是 Java 镜像，有的 400MB，有的只有 90MB？

涉及：

* jlink 裁剪 JRE
* distroless
* CDS（类数据共享）
* 分层缓存

这些属于“平台工程师开始拉开差距”的技能。
