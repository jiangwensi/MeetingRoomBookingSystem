package com.jiangwensi.mrbs.model.request.auth;

/**
 * Created by Jiang Wensi on 21/8/2020
 */
public class ResetPasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {

        String maskedOldPassword = (oldPassword == null) ? null : oldPassword.replaceAll("\\*", "*");
        String maskedNewPassword = (newPassword == null) ? null : newPassword.replaceAll("\\*", "*");

        return "ResetPasswordRequest{" +
                "email='" + email + '\'' +
                ", oldPassword='" + maskedOldPassword + '\'' +
                ", newPassword='" + maskedNewPassword + '\'' +
                '}';
    }
}
