package com.authservice.authservice.Serivce;

import com.authservice.authservice.Model.User;

public interface UserService {
    String login(User user);
    String register(User user);
}
