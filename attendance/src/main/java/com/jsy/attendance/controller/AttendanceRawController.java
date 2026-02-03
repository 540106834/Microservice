package com.jsy.attendance.controller;

import com.jsy.attendance.service.AttendanceRawService;
import org.springframework.web.bind.annotation.*;
import com.jsy.attendance.dto.RawDataDTO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/raw")
public class AttendanceRawController {

    private final AttendanceRawService rawService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AttendanceRawController(AttendanceRawService rawService) {
        this.rawService = rawService;
    }

    /**
     * 测试接口
     */
    @GetMapping("/health")
    public String health() {
        return "200 ok service is running";
    }

    /**
     * 接收原始打卡数据
     */
    @PostMapping("/save")
    public String saveRaw(@RequestBody RawDataDTO dto) {
        try {
            LocalDateTime eventTime = LocalDateTime.parse(dto.getEventTime(), formatter);
            rawService.saveRawData(dto.getDeviceSn(), dto.getCardNo(), eventTime, dto.getEventType(), dto.getSn());
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }
}
