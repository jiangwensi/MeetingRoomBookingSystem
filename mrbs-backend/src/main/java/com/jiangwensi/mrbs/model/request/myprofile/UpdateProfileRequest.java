package com.jiangwensi.mrbs.model.request.myprofile;

/**
 * Created by Jiang Wensi on 22/8/2020
 */
public class UpdateProfileRequest {
    private String publicId;
    private String name;

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

    @Override
    public String toString() {
        return "UpdateProfileRequest{" +
                "publicId='" + publicId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
