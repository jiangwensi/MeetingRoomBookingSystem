package com.jiangwensi.mrbs.model.response.auth;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.Date;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
public class SignUpResponse extends GeneralResponse {
    private String userId;
    private String name;
    private String email;
    private Date time;

    public SignUpResponse() {
        this.time = new Date();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

}
