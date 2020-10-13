package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.BookingDto;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public interface BookingService {


//    List<BookingDto> searchBooking(String bookedBy, String roomName, String fromDate, String toDate);

    BookingDto createBooking(String bookedBy, String roomId, String from, String to) throws ParseException;

    BookingDto updateBooking(String publicId, String room, String fromTime, String toTime) throws ParseException;

    BookingDto viewBooking(String bookingId);

    void deleteBooking(String publicId);

    List<BookingDto> search(Boolean isSysAdm, String bookedBy, String roomId, String fromDate,String toDate);
}
