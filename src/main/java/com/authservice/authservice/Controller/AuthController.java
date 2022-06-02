package com.authservice.authservice.Controller;

import com.authservice.authservice.Model.RefreshToken;
import com.authservice.authservice.Model.User;
import com.authservice.authservice.Repository.UserRepository;
import com.authservice.authservice.Serivce.RefreshTokenService;
import com.authservice.authservice.Serivce.UserService;
import com.authservice.authservice.auth.JwtUserDetailsImpl;
import com.authservice.authservice.exception.TokenRefreshException;
import com.authservice.authservice.request.JwtRequest;
import com.authservice.authservice.auth.JwtResponse;
import com.authservice.authservice.auth.JwtUserDetailsService;
import com.authservice.authservice.auth.JwtUtil;
import com.authservice.authservice.request.TokenRefreshRequest;
import com.authservice.authservice.response.TokenRefreshResponse;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception{
        try{
          User user = userRepository.findByEmail(authenticationRequest.getEmail());
        if(user != null && encoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

            final JwtUserDetailsImpl userDetails = (JwtUserDetailsImpl) userDetailsService
                    .loadUserByUsername(authenticationRequest.getEmail());
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            final String token = jwtUtil.generateToken(userDetails);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            return ResponseEntity.ok(new JwtResponse(token, user.getId(), roles.get(0), refreshToken.getToken()));
        }
        else{
            throw new Exception("Invalid credentials");
        }
        }
        catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }

    }

    @GetMapping("getOneByEmail/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email);

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
        user.setPassword(encoder.encode(user.getPassword()));


        return userService.register(user);
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {

                    String token = jwtUtil.generateTokenFromEmail(user.getEmail());
                    TokenRefreshResponse tokenRefreshResponse = null;
                    try{

                        tokenRefreshResponse = new TokenRefreshResponse(token, requestRefreshToken);
                        return ResponseEntity.ok(tokenRefreshResponse);
                    }
                    catch (Exception e){
                        e.toString();
                        return null;
                    }

                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Long id){
        return userService.deleteById(id);
    }

}
