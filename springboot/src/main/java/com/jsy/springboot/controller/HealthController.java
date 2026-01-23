package com.jsy.springboot.controller;

import com.jsy.springboot.dto.Result;
import com.jsy.springboot.service.HealthService;
import com.jsy.springboot.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HealthController {

    private static final Logger log = LogUtil.getLogger(HealthController.class);
    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public Result health(
            @RequestParam(required = false) Integer cpu
    ) {
        log.info("health check request, cpu={}", cpu);

        if (cpu == null) {
            log.warn("cpu param missing");
            return Result.error("cpu param required");
        }

        boolean ok = healthService.isHealthy(cpu);

        if (!ok) {
            log.error("cpu overload: {}%", cpu);
            return Result.error("CPU overload");
        }

        return Result.success("OK");
    }
}
