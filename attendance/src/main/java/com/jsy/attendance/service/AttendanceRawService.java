package com.jsy.attendance.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AttendanceRawService {

    private final JdbcTemplate jdbcTemplate;

    public AttendanceRawService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveRawData(String deviceSn, String cardNo, LocalDateTime eventTime, int eventType, String sn) {
        String sql = "INSERT INTO attendance_raw(device_sn, card_no, event_time, event_type, sn, parsed, received_at) "
                +
                "VALUES (?, ?, ?, ?, ?, 0, NOW())";
        jdbcTemplate.update(sql, deviceSn, cardNo, eventTime, eventType, sn);
        System.out.println("Raw 数据已写入: " + deviceSn + " -> " + cardNo + " -> " + eventTime);
    }
}
