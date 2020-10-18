package com.jiangwensi.mrbs.model.request.auth;

/**
 * Created by Jiang Wensi on 17/8/2020
 */
public class LoginRequest {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + maskedPassword+"'}";
    }
}
