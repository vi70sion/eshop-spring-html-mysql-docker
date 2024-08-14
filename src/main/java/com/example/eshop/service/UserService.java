package com.example.eshop.service;

import com.example.eshop.JwtDecoder;
import com.example.eshop.model.User;
import com.example.eshop.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserService {

    UserRepository userRepository = new UserRepository();

    public int userLogin(User user) {
        return userRepository.userLogin(user);
    }


    public static boolean authorize(String authorizationHeader){
        try {
            JwtDecoder.decodeJwt(authorizationHeader);
        } catch (JwtException e) {
            return false;
        }
        return true;
    }

    public static boolean isTokenCorrect(String authorizationHeader){
        return (authorizationHeader.length() < 20 || authorizationHeader == null || authorizationHeader.isEmpty()) ? false : true;
    }

    public static ResponseEntity<String> handleAuthorization(String authorizationHeader) {
        if (!isTokenCorrect(authorizationHeader)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("bad request");
        }
        if (!authorize(authorizationHeader)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("unauthorized");
        }
        return ResponseEntity.ok("authorized");
    }

}
