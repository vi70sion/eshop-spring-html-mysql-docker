package com.example.eshop.service;

import com.example.eshop.JwtDecoder;
import com.example.eshop.model.User;
import com.example.eshop.repository.UserRepository;
import io.jsonwebtoken.JwtException;

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
}
