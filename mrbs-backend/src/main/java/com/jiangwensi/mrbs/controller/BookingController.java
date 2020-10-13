package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.dto.BookingDto;
import com.jiangwensi.mrbs.dto.RoomDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.exception.AccessDeniedException;
import com.jiangwensi.mrbs.model.request.booking.BookingRequest;
import com.jiangwensi.mrbs.model.response.GeneralResponse;
import com.jiangwensi.mrbs.model.response.booking.BookingResponse;
import com.jiangwensi.mrbs.model.response.booking.SearchBookingResponse;
import com.jiangwensi.mrbs.model.response.booking.SearchBookingResponseItem;
import com.jiangwensi.mrbs.service.BookingService;
import com.jiangwensi.mrbs.service.RoomService;
import com.jiangwensi.mrbs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

//    @GetMapping("/sysadm")
//    @PreAuthorize("hasAuthority('SYSADM')")
//    public SearchBookingResponse searchBookingBySysAdm(@RequestParam(required = false) String bookedBy,
//                                                       @RequestParam(required = false) String roomId,
//                                                       @RequestParam(required = false) String date) {
//        log.info("searchBookingBySysAdm bookedBy:" + bookedBy + ",roomId:" + roomId + ",date:" + date);
//
//        List<BookingDto> bookingDtos = bookingService.search(bookedBy, roomId, date);
//        SearchBookingResponse returnValue = new SearchBookingResponse();
//        List<SearchBookingResponseItem> bookings = new ModelMapper().map(bookingDtos, new TypeToken<ArrayList<SearchBookingResponseItem>>() {
//        }.getType());
//        returnValue.setBookings(bookings);
//        returnValue.setStatus(MyResponseStatus.success.name());
//        returnValue.setMessage("Search booking is successful");
//        return returnValue;
//    }

    @GetMapping
    public SearchBookingResponse searchBooking(@RequestParam(required = false) String roomName,
                                                 @RequestParam(required = false) String fromDate,
                                                 @RequestParam(required = false) String toDate) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        boolean isSysAdm =
                auth.getAuthorities().contains(new SimpleGrantedAuthority("SYSADM"));
        List<BookingDto> bookingDtos = bookingService.search(isSysAdm,userDto.getPublicId(), roomName, fromDate,toDate);
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
        log.info("createBooking " + request.toString());

        UserDto userDto = getMyDto();

        if (!isAccessedByRoomUser(request.getRoomId())&&!isAccessedByTargetRole("SYSADM")) {
            throw new AccessDeniedException("You are not allowed to book this room:" + request.getRoomId());
        }

        BookingDto bookingDto = bookingService.createBooking(userDto.getPublicId(), request.getRoomId(), request.getFromTime(), request.getToTime());

        BookingResponse returnValue = new BookingResponse();

        new ModelMapper().map(bookingDto, returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Booking is created successfully");

        return returnValue;
    }

    @PatchMapping
    public BookingResponse updateBooking(@RequestBody BookingRequest request) throws ParseException {
        log.info("updateBooking " + request.toString());


        if (!isAccessingMyBooking(request.getPublicId())
                && !isAccessedByRoomAdmin(request.getPublicId())) {
            throw new AccessDeniedException("You are not allowed to update this booking:" + request.getPublicId());
        }

        BookingDto bookingDto = bookingService.updateBooking(request.getPublicId(), request.getRoomId(),
                request.getFromTime(), request.getToTime());
        BookingResponse returnValue = new BookingResponse();

        new ModelMapper().map(bookingDto, returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Update booking is successful.");
        return returnValue;
    }

    @GetMapping("/{publicId}")
    public BookingResponse viewBooking(@PathVariable String publicId) throws ParseException {
        log.info("viewBooking publicId:" + publicId);


        if (!isAccessingMyBooking(publicId)
                && !isAccessedByRoomAdmin( publicId)
        ) {

            throw new AccessDeniedException("You are not allowed to view this booking:" + publicId);

        }
        BookingDto bookingDto = bookingService.viewBooking(publicId);
        BookingResponse returnValue = new BookingResponse();

        new ModelMapper().map(bookingDto, returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("View booking is successful.");
        return returnValue;
    }

    @DeleteMapping("/{publicId}")
    public GeneralResponse deleteBooking(@PathVariable String publicId) throws ParseException {
        log.info("deleteBooking publicId:" + publicId);

        if (!isAccessingMyBooking(publicId) && !isAccessedByRoomAdmin(publicId)) {
            throw new AccessDeniedException("You are not allowed to delete this booking:" + publicId);
        }

        bookingService.deleteBooking(publicId);

        GeneralResponse returnValue = new GeneralResponse();
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Delete booking is successful.");
        return returnValue;
    }

    private boolean isAccessedByRoomUser(String roomId) {
        UserDto userDto = getMyDto();

        RoomDto roomDto = roomService.viewRoom(roomId);
        List<String> roomUsers = roomDto.getUsers();
        if (roomUsers != null && roomUsers.size() > 0) {
            for (String roomUser : roomUsers) {
                if (roomUser.equals(userDto.getPublicId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAccessedByTargetRole(String role) {

        UserDto userDto = getMyDto();
        List<String> roles = userDto.getRoles();
        if (roles != null && roles.size() > 0) {
            for (String e : roles) {
                if (e.equals(role)) {
                    return true;
                }
            }
        }
        ;
        return false;
    }

    private boolean isAccessedByRoomAdmin(String roomId) {

        UserDto userDto = getMyDto();

        RoomDto roomDto = roomService.viewRoom(roomId);
        List<String> roomAdms = roomDto.getAdmins();
        if (roomAdms != null && roomAdms.size() > 0) {
            for (String roomAdm : roomAdms) {
                if (roomAdm.equals(userDto.getPublicId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAccessingMyBooking(String bookingId) {

        UserDto userDto = getMyDto();

        BookingDto bookingDto = bookingService.viewBooking(bookingId);
        if (userDto.getPublicId().equals(bookingDto.getBookedBy())) {
            return true;
        }
        return false;
    }

    private UserDto getMyDto() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByEmail(auth.getName());
    }

}
