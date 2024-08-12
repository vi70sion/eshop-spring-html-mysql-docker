package com.example.eshop.service;

import com.example.eshop.model.User;
import com.example.eshop.repository.UserRepository;

public class UserService {

    UserRepository userRepository = new UserRepository();

    public int userLogin(User user) {
        return userRepository.userLogin(user);
    }

}
