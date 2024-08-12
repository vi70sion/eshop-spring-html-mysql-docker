package com.example.eshop.controller.controller;

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
                new ResponseEntity<>("null", HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(JwtGenerator.generateJwt(user.getName(), user.getPassword(), userId), HttpStatus.OK);
    }

}
