package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.AvailableTimeslotDto;
import com.jiangwensi.mrbs.dto.BookingDto;
import com.jiangwensi.mrbs.dto.RoomDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.model.request.room.BlockedTimeSlot;

import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public interface RoomService {
    List<RoomDto> searchRoom(String name, String orgName,Boolean active);

    RoomDto viewRoom(String publicId);

    RoomDto createRoom(String name, Integer capacity, String facilities, String description, Boolean active,
                       String organization,
                       List<String> admins,List<BlockedTimeSlot> blockedTimeslots);

    RoomDto updateRoom(String publicId, String name, Integer capacity, String facilities, String description,
                       Boolean active,
                       String organization,
                       List<String> admins,
                       List<String> users,List<BlockedTimeSlot> blockedTimeslots);

    void deleteRoom(String publicId);

    List<UserDto> enrollUser(String roomPublicId, List<String> users);

    List<UserDto> unenrollUser(String roomPublicId, List<String> users);

    List<UserDto> listEnrolledUsers(String roomId);

    List<UserDto> listRoomUsers(String roomPublicId);

    List<UserDto> listRoomAdmins(String roomPublicId);

    List<BookingDto> listRoomBookings(String roomPublicId);

    List<AvailableTimeslotDto> listAvailableTimeslots(String roomPublicId, String date);
}
