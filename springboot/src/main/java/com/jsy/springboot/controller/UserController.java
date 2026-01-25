package com.jsy.springboot.controller;

//import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.jsy.springboot.dto.UserDTO;
import com.jsy.springboot.dto.Result;
import com.jsy.springboot.service.UserService;
//import org.slf4j.Logger;

@RestController
@RequestMapping("/user")
public class UserController {

    // private static final Logger log =
    // LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    // 构造器注入
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<UserDTO> registerUser(@RequestBody UserDTO user) {
        try {
            UserDTO result = userService.register(user);
            return Result.success(result);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }
}
