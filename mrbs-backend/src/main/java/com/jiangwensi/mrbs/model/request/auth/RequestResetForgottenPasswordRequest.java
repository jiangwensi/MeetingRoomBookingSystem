package com.jiangwensi.mrbs.model.request.auth;

/**
 * Created by Jiang Wensi on 20/8/2020
 */
public class RequestResetForgottenPasswordRequest {
    private String email;
    private String returnUrl;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Override
    public String toString() {
        return "RequestResetForgottenPasswordRequest{" +
                "email='" + email + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                '}';
    }
}
