package com.jiangwensi.mrbs.model.response.room;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 6/9/2020
 */
public class AvailableTimeslotResponse extends GeneralResponse {
    List<AvailableTimeslot> availableTimeslots = new ArrayList();

    public List<AvailableTimeslot> getAvailableTimeslots() {
        return availableTimeslots;
    }

    public void setAvailableTimeslots(List<AvailableTimeslot> availableTimeslots) {
        this.availableTimeslots = availableTimeslots;
    }
}
