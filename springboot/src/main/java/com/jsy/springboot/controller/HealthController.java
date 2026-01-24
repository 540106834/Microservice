package com.jsy.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        boolean serviceOk = true;
        return serviceOk ? "UP" : "DOWN";
    }
}
