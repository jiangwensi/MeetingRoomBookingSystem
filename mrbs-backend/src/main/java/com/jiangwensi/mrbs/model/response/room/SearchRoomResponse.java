package com.jiangwensi.mrbs.model.response.room;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public class SearchRoomResponse extends GeneralResponse {

    List<SearchRoomResponseItem> rooms = new ArrayList<>();

    public List<SearchRoomResponseItem> getRooms() {
        return rooms;
    }

    public void setRooms(List<SearchRoomResponseItem> rooms) {
        this.rooms = rooms;
    }
}
