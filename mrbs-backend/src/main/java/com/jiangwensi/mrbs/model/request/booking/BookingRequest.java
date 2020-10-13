package com.jiangwensi.mrbs.model.request.booking;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public class BookingRequest {

    private String publicId;
    private String fromTime;
    private String toTime;
    private String roomId;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "BookingRequest{" +
                "publicId='" + publicId + '\'' +
                ", fromTime='" + fromTime + '\'' +
                ", toTime='" + toTime + '\'' +
                ", room='" + roomId + '\'' +
                '}';
    }
}
