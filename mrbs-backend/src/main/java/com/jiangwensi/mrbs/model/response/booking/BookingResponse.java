package com.jiangwensi.mrbs.model.response.booking;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.Date;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public class BookingResponse extends GeneralResponse {

    private String publicId;
    private Date fromTime;
    private Date toTime;
    private String room;
    private String bookedBy;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getToTime() {
        return toTime;
    }

    public void setToTime(Date toTime) {
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
