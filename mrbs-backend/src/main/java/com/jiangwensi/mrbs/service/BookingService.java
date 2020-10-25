package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.AvailableTimeslotDto;
import com.jiangwensi.mrbs.dto.BookingDto;
import com.jiangwensi.mrbs.model.request.booking.BookingRequest;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public interface BookingService {


    BookingDto createBooking(BookingRequest bookingRequest) throws ParseException;

    BookingDto updateBooking(BookingRequest bookingRequest) throws ParseException;

    BookingDto viewBooking(String bookingId);

    void deleteBooking(String publicId);

    List<BookingDto> search(String roomId, String date);

    List<AvailableTimeslotDto> fetchAvailableslotByRoom(String roomId, String date);
}
