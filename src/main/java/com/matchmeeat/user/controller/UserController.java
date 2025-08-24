package com.matchmeeat.user.controller;

import com.matchmeeat.user.dto.UserDto;
import com.matchmeeat.user.entity.User;
import com.matchmeeat.user.mapper.UserMapper;
import com.matchmeeat.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserProfile(@RequestParam String username) {
        User user = userService.findUserByUsername(username);
        return ResponseEntity.ok(userMapper.userToUserDto(user));
    }
}
