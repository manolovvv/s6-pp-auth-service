package com.authservice.authservice.Serivce;

import com.authservice.authservice.Model.User;
import com.authservice.authservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String login(User user) {
        return null;
    }

    @Override
    public String register(User user) {
        try {
            userRepository.save(user);
            return "success";
        }
        catch(DataIntegrityViolationException e){
           return "Email already taken";
        }
    }
}
