package com.authservice.authservice.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expiryDate;
    //getters and setters
}