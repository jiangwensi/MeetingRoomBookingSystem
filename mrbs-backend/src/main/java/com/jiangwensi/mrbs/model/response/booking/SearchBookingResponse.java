package com.jiangwensi.mrbs.model.response.booking;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public class SearchBookingResponse extends GeneralResponse {
    private List<SearchBookingResponseItem> bookings = new ArrayList<>();

    public List<SearchBookingResponseItem> getBookings() {
        return bookings;
    }

    public void setBookings(List<SearchBookingResponseItem> bookings) {
        this.bookings = bookings;
    }
}
