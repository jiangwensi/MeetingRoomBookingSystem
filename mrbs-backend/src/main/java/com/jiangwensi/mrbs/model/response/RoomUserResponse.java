package com.jiangwensi.mrbs.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public class RoomUserResponse extends GeneralResponse {
    private List<String> users = new ArrayList<>();
    private String roomPublicId;

    public String getRoomPublicId() {
        return roomPublicId;
    }

    public void setRoomPublicId(String roomPublicId) {
        this.roomPublicId = roomPublicId;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
