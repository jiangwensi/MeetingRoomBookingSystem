package com.jiangwensi.mrbs.dto;

/**
 * Created by Jiang Wensi on 3/9/2020
 */
public class BlockedTimeslotDto {
    private String type;
    private String day;
    private String timeFrom;
    private String timeTo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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
}
