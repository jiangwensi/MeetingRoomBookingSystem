package com.jiangwensi.mrbs.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jiang Wensi on 15/8/2020
 */
@Entity
@Table(name = "booking",indexes = {
        @Index(name="booking_date_index",columnList = "date"),
        @Index(name="booking_room_id_index",columnList = "room_id"),
        @Index(name="booking_to_time_index",columnList = "toTime"),
        @Index(name="booking_from_time_index",columnList = "fromTime")
})
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String publicId;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Date fromTime;

    @Column(nullable = false)
    private Date toTime;

    @ManyToOne
    @JoinColumn(name="room_id")
    private RoomEntity room;

    @ManyToOne
    @JoinColumn(name = "bookedBy")
    private UserEntity bookedBy;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public UserEntity getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(UserEntity bookedBy) {
        this.bookedBy = bookedBy;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
