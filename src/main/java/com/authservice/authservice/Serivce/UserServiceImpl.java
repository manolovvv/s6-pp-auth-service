package com.authservice.authservice.Serivce;

import com.authservice.authservice.Model.User;
import com.authservice.authservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String login(User user) {
        User foundUser = userRepository.findOne(user);
        return null;
    }

    @Override
    public String register(User user) {
        return null;
    }
}
