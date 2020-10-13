package com.jiangwensi.mrbs.dto;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
public class AvatarDto {
    private String id;
    private Byte[] avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(Byte[] avatar) {
        this.avatar = avatar;
    }
}
