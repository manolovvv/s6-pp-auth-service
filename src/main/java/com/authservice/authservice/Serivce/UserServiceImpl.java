package com.authservice.authservice.Serivce;

import com.authservice.authservice.Model.User;
import com.authservice.authservice.Repository.UserRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String login(User user) {
        return null;
    }

    @KafkaListener(topics= {"deleteUser"}, groupId="deleteUser")
    public void consume(ConsumerRecord<String, String> record)  {

        userRepository.deleteById(new Long(record.value().toString()));
    }

    @Override
    public String register(User user) {
        try {

            return userRepository.save(user).getId().toString();
        }
        catch(DataIntegrityViolationException e){
           return "Email already taken";
        }
    }

    @Override
    public String deleteById(Long id) {
        userRepository.deleteById(id);
        return "success";
    }
}
