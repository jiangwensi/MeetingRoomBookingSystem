package com.jiangwensi.mrbs.model.response.user;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 31/8/2020
 */
public class ListUserResponse extends GeneralResponse {
    List<UserResponseItem> users = new ArrayList<>();

    public List<UserResponseItem> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseItem> users) {
        this.users = users;
    }
}
