package com.jiangwensi.mrbs.model.response.booking;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public class BookingResponse extends GeneralResponse {

    private String publicId;
    private String fromTime;
    private String toTime;
    private String room;
    private String bookedBy;

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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }
}
