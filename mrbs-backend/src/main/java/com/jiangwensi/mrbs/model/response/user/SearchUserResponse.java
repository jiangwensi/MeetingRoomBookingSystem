package com.jiangwensi.mrbs.model.response.user;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 22/8/2020
 */
public class SearchUserResponse extends GeneralResponse {
    private List<UserResponse> users = new ArrayList<>();

    public List<UserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponse> users) {
        this.users = users;
    }
}
