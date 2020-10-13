package com.jiangwensi.mrbs.model.request.auth;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String returnUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Override
    public String toString() {
        String maskedPassword = (password == null) ? null : password.replaceAll("*", "*");
        return "SignUpRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + maskedPassword + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                '}';
    }
}
