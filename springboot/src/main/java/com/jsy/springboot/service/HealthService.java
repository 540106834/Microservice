package com.jsy.springboot.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public boolean isHealthy(int cpu) {
        return cpu < 80;
    }
}
