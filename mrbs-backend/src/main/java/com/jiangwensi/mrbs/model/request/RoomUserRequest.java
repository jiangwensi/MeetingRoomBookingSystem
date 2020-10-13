package com.jiangwensi.mrbs.model.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public class RoomUserRequest {

    private List<String> users = new ArrayList<>();
    private String action;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "RoomUserRequest{" +
                ", users=" + users +
                ", action='" + action + '\'' +
                '}';
    }
}
