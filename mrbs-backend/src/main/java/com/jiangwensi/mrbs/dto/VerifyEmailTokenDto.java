package com.jiangwensi.mrbs.dto;

/**
 * Created by Jiang Wensi on 17/8/2020
 */
public class VerifyEmailTokenDto {
    private boolean isValid;
    private String message;
    private String email;
    private String returnUrl;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
}
