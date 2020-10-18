package com.jiangwensi.mrbs.model.request.auth;

/**
 * Created by Jiang Wensi on 21/8/2020
 */
public class ResetForgottenPasswordRequest {
    private String token;
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        String maskedPassword = (password == null) ? null : password.replaceAll("\\*", "*");
        return "ResetForgottenPasswordRequest{" +
                "token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", password='" + maskedPassword + '\'' +
                '}';
    }
}
