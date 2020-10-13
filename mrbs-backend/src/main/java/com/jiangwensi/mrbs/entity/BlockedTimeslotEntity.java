package com.jiangwensi.mrbs.entity;

import javax.persistence.*;

/**
 * Created by Jiang Wensi on 3/9/2020
 */
@Entity
@Table(name="blocked_timeslot")
public class BlockedTimeslotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //ONCE,DAILY_REPEAT,WEEKLY_REPEAT,MONTHLY_REPEAT

    @Column(nullable = false)
    private String type;

    //ONCE:date
    //DAILY_REPEAT:n/a
    //WEEKLY_REPEAT:day in a week (MON,TUE,WED,THUR,FRI,SAT,SUN)
    //MONTHLY_REPEAT:day in a month (1-31)
    @Column(nullable = true)
    private String day;

    @Column(nullable = false)
    private String timeFrom;

    @Column(nullable = false)
    private String timeTo;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String date) {
        this.day = date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }
}
