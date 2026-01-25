package com.jsy.springboot.service;

import org.springframework.stereotype.Service;
import com.jsy.springboot.dto.UserDTO;

@Service
public class UserService {

    public UserDTO register(UserDTO user) {

        // 业务校验
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        return user;
    }
}