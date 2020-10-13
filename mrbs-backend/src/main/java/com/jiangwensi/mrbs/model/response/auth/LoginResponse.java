package com.jiangwensi.mrbs.model.response.auth;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.Set;

/**
 * Created by Jiang Wensi on 26/8/2020
 */
public class LoginResponse extends GeneralResponse {
    private String authToken;
    private String name;
    private Set<String> roles;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
