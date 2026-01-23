// package com.jsy.demo;

// import org.aspectj.lang.ProceedingJoinPoint;
// import org.aspectj.lang.annotation.*;
// import org.slf4j.*;
// import org.springframework.stereotype.Component;

// @Aspect
// @Component
// public class LoggingAspect {

//     private static final Logger logger = LoggerFactory.getLogger("CRUD");

//     @Around("@within(org.springframework.web.bind.annotation.RestController)")
//     public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//         long start = System.currentTimeMillis();

//         Object result = joinPoint.proceed();

//         long duration = System.currentTimeMillis() - start;

//         // 最简单日志：方法名 + 耗时
//         logger.info("{} executed in {} ms", joinPoint.getSignature(), duration);

//         return result;
//     }
// }

package com.jsy.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger("CRUD");

    // 用 Jackson 序列化对象为 JSON，方便日志打印
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // 打印参数
        String argsJson = "[]";
        try {
            argsJson = objectMapper.writeValueAsString(joinPoint.getArgs());
        } catch (Exception e) {
            logger.warn("Failed to serialize arguments: {}", e.getMessage());
        }
        logger.info("Entering {} with args={}", joinPoint.getSignature(), argsJson);

        // 执行 Controller 方法
        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;

        // 打印返回值
        String resultJson = "";
        try {
            resultJson = objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            logger.warn("Failed to serialize result: {}", e.getMessage());
        }
        logger.info("Exiting {} with result={} ({} ms)", joinPoint.getSignature(), resultJson, duration);

        return result;
    }
}


