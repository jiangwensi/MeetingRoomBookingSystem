package com.jiangwensi.mrbs.dto;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
public class RoomImageDto {
    private String id;
    private RoomDto room;
    private Byte[] image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoomDto getRoom() {
        return room;
    }

    public void setRoom(RoomDto room) {
        this.room = room;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }
}
