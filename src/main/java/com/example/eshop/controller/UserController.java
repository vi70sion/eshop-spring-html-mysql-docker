package com.example.eshop.controller;

import com.example.eshop.JwtGenerator;
import com.example.eshop.model.User;
import com.example.eshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    UserService userService = new UserService();
    public UserController() {
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody User user) {
        int userId = userService.userLogin(user);
        return (userId == -1) ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("null") :
                ResponseEntity.ok(JwtGenerator.generateJwt(user.getName(), user.getPassword(), userId));
    }

}
