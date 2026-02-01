# **Spring Boot 项目里跑一个 TCP Server**
先测试“服务能接收请求”而不是 HTTP。
下面我给你一套**最简单可执行的 TCP Server 实现**，可以直接在 Spring Boot 项目里跑通。

---

## 1️ 新建 Spring Boot 项目

假设项目已经有 `AttendanceApplication` 主类。

---

## 2️ TCP Server 配置类

```java
package com.example.attendance.tcp;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简单 TCP Server
 * 默认端口 9999
 */
@Component
public class TcpServer {

    private ServerSocket serverSocket;
    private volatile boolean running = true;

    @PostConstruct
    public void start() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(9999);
                System.out.println("TCP Server 已启动，端口：9999");

                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("客户端连接：" + clientSocket.getInetAddress());

                    // 每个连接开一个线程处理
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (Exception e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("收到客户端消息：" + line);
                // 简单应答
                if ("ping".equalsIgnoreCase(line.trim())) {
                    writer.println("ok");
                } else {
                    writer.println("你发的内容是：" + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("客户端断开连接");
            } catch (Exception ignored) {}
        }
    }

    @PreDestroy
    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            System.out.println("TCP Server 已停止");
        } catch (Exception ignored) {}
    }
}
```

---

## 3️ 测试 TCP Server

1. 启动 Spring Boot 项目
2. 用 **telnet 或 nc** 测试：

```bash
# 使用 nc 测试
nc localhost 9999
```

3. 输入 `ping`，回车：

```text
ping
ok
```

4. 输入任意其他内容：

```text
hello
你发的内容是：hello
```

 **说明**

* TCP Server 可以接收客户端连接
* 可以响应测试消息
* Spring Boot 项目和 TCP 服务共存

---

## 4️ 后续可扩展点

* 改为 **异步处理、线程池**
* 支持 **学生打卡数据传输**
* 可和 **数据库 + Service 层** 对接，做到 TCP → Service → Repository → MySQL

---

如果你愿意，我可以帮你写**“TCP Server 接收学生打卡消息 → 入库 → 返回 ok”完整流程示例**，直接跑通整个业务链。

你希望我直接写吗？
