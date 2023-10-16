package com.moditech.ecommerce.controller;


import com.moditech.ecommerce.dto.LoginDto;
import com.moditech.ecommerce.model.User;
import com.moditech.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Value("${frontend.base.url}")
    String frontEndBaseUrl;


    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody LoginDto loginDto) throws Exception {
        return userService.loginUser(loginDto.getEmail(), loginDto.getPassword());
    }

    @GetMapping("/list")
    public List<User> getListUserController() {
        return userService.getListUser();
    }

    @GetMapping("/{email}")
    private User getUserByEmail(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }

    @PutMapping("/changePassword/{email}")
    public ResponseEntity<String> updatePassword(@PathVariable("email") String email, @RequestBody User user) {
        userService.updatePassword(email, user);
        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }

    @GetMapping("/isEnable/userID/{email}")
    private void updateIsEnableUser(@PathVariable String email, HttpServletResponse response) throws IOException {
        User user = userService.updateIsEnableUser(email);
        response.sendRedirect(frontEndBaseUrl);
    }

}
