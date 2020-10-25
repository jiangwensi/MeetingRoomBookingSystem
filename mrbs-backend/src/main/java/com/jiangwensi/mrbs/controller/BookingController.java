package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.dto.AvailableTimeslotDto;
import com.jiangwensi.mrbs.dto.BookingDto;
import com.jiangwensi.mrbs.model.request.booking.BookingRequest;
import com.jiangwensi.mrbs.model.response.GeneralResponse;
import com.jiangwensi.mrbs.model.response.booking.BookingResponse;
import com.jiangwensi.mrbs.model.response.booking.SearchBookingResponse;
import com.jiangwensi.mrbs.model.response.booking.SearchBookingResponseItem;
import com.jiangwensi.mrbs.model.response.room.AvailableTimeslot;
import com.jiangwensi.mrbs.model.response.room.AvailableTimeslotResponse;
import com.jiangwensi.mrbs.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
@RestController
@Slf4j
@RequestMapping(PathConst.BOOKING_PATH)
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Scope("request")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/room/{roomId}/availableslots/{date}")
    public AvailableTimeslotResponse fetchAvailableslotByRoom(
            @PathVariable("roomId") String roomId,
            @PathVariable(value="date",required = false)  String date) {
        List<AvailableTimeslotDto> dtos = bookingService.fetchAvailableslotByRoom(roomId,date);
        List<AvailableTimeslot> responseItem = new ModelMapper().map(dtos, new TypeToken<ArrayList<AvailableTimeslot>>(){}.getType());

        AvailableTimeslotResponse response = new AvailableTimeslotResponse();
        response.setAvailableTimeslots(responseItem);
        response.setStatus(MyResponseStatus.success.name());
        return response;
    }

    @GetMapping
    public SearchBookingResponse searchBooking(
            @RequestParam(required = false) String roomId,
            @RequestParam(required = false) String date) {

        List<BookingDto> bookingDtos = bookingService.search(roomId, date);
        SearchBookingResponse returnValue = new SearchBookingResponse();
        List<SearchBookingResponseItem> bookings = new ModelMapper().map(bookingDtos, new TypeToken<ArrayList<SearchBookingResponseItem>>() {
        }.getType());
        returnValue.setBookings(bookings);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Search booking is successful");
        return returnValue;
    }

    @PostMapping
    public BookingResponse createBooking(@RequestBody BookingRequest request) throws ParseException {
        BookingDto bookingDto = bookingService.createBooking(request);
        BookingResponse returnValue = new BookingResponse();

        new ModelMapper().map(bookingDto, returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Booking is created successfully");

        return returnValue;
    }

    @PatchMapping
    public BookingResponse updateBooking(@RequestBody BookingRequest request) throws ParseException {

        BookingDto bookingDto = bookingService.updateBooking(request);
        BookingResponse returnValue = new BookingResponse();

        new ModelMapper().map(bookingDto, returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Update booking is successful.");
        return returnValue;
    }

    @GetMapping("/{publicId}")
    public BookingResponse viewBooking(@PathVariable String publicId) {

        BookingDto bookingDto = bookingService.viewBooking(publicId);
        BookingResponse returnValue = new BookingResponse();

        new ModelMapper().map(bookingDto, returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("View booking is successful.");
        return returnValue;
    }

    @DeleteMapping("/{publicId}")
    public GeneralResponse deleteBooking(@PathVariable String publicId){
        bookingService.deleteBooking(publicId);

        GeneralResponse returnValue = new GeneralResponse();
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Delete booking is successful.");
        return returnValue;
    }

}
