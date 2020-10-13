package com.jiangwensi.mrbs.model.request.user;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

/**
 * Created by Jiang Wensi on 29/8/2020
 */
public class UpdateMyProfileRequest extends GeneralResponse {
    private String publicId;
    private String name;
    private String email;
    private String changeEmailReturnUrl;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

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

    public String getChangeEmailReturnUrl() {
        return changeEmailReturnUrl;
    }

    public void setChangeEmailReturnUrl(String changeEmailReturnUrl) {
        this.changeEmailReturnUrl = changeEmailReturnUrl;
    }
}
