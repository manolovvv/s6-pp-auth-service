package com.authservice.authservice;

import com.authservice.authservice.Model.Role;
import com.authservice.authservice.Model.User;
import com.authservice.authservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {


	@Autowired
	PasswordEncoder encoder;


	public static void main(String[] args) {

		SpringApplication.run(AuthServiceApplication.class, args);

	}


}
