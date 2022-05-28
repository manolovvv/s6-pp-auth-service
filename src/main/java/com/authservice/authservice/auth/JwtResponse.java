package com.authservice.authservice.auth;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final Long id;
    private final String role;
    private final String refreshToken;

    public JwtResponse(String jwttoken, Long id, String role, String refreshToken) {
        this.jwttoken = jwttoken;
        this.id = id;
        this.role = role;
        this.refreshToken = refreshToken;
    }



    public String getJwttoken() {
        return jwttoken;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
