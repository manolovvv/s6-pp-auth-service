package com.authservice.authservice.Controller;

import com.authservice.authservice.Model.User;
import com.authservice.authservice.Repository.UserRepository;
import com.authservice.authservice.Serivce.UserService;
import com.authservice.authservice.auth.JwtRequest;
import com.authservice.authservice.auth.JwtResponse;
import com.authservice.authservice.auth.JwtUserDetailsService;
import com.authservice.authservice.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception{
        try{
          User user = userRepository.findByEmail(authenticationRequest.getEmail());
            System.out.println(user);
        if(user != null && encoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
            System.out.println(authenticationRequest.toString());

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getEmail());
            System.out.println("qvno da");

            final String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(token));
        }
        else{
            throw new Exception("Invalid credentials");
        }
        }
        catch (Exception e){
            return (ResponseEntity<?>) ResponseEntity.ok(e.getMessage());
        }

    }

    @GetMapping("/")
    public String test(){
        return "test";
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody User user){
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));

        return userService.register(user);
    }

}
