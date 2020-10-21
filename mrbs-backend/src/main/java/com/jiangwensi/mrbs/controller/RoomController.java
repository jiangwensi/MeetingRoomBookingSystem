package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.dto.AvailableTimeslotDto;
import com.jiangwensi.mrbs.dto.BookingDto;
import com.jiangwensi.mrbs.dto.RoomDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.exception.AccessDeniedException;
import com.jiangwensi.mrbs.exception.InvalidInputException;
import com.jiangwensi.mrbs.model.request.RoomUserRequest;
import com.jiangwensi.mrbs.model.request.room.BlockedTimeSlot;
import com.jiangwensi.mrbs.model.request.room.RoomRequest;
import com.jiangwensi.mrbs.model.response.GeneralResponse;
import com.jiangwensi.mrbs.model.response.RoomUserResponse;
import com.jiangwensi.mrbs.model.response.booking.SearchBookingResponse;
import com.jiangwensi.mrbs.model.response.booking.SearchBookingResponseItem;
import com.jiangwensi.mrbs.model.response.room.AvailableTimeslotResponse;
import com.jiangwensi.mrbs.model.response.room.RoomResponse;
import com.jiangwensi.mrbs.model.response.room.SearchRoomResponse;
import com.jiangwensi.mrbs.model.response.room.SearchRoomResponseItem;
import com.jiangwensi.mrbs.model.response.user.SearchUserResponse;
import com.jiangwensi.mrbs.model.response.user.UserResponse;
import com.jiangwensi.mrbs.service.OrgService;
import com.jiangwensi.mrbs.service.RoomService;
import com.jiangwensi.mrbs.service.UserService;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
@RestController
@Slf4j
@RequestMapping(PathConst.ROOM_PATH)
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Scope("request")
public class RoomController extends BaseController {
    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    OrgService orgService;

    @GetMapping
    public SearchRoomResponse search(@RequestParam(required = false, value = "roomName") String name,
                                     @RequestParam(required = false, value = "orgName") String orgName,
                                     @RequestParam(required = false) Boolean active) {
        log.info("search name:" + name + ",active:" + active);

        List<RoomDto> roomDtos = roomService.searchRoom(name, orgName, active);

        List<SearchRoomResponseItem> items = new ModelMapper().map(roomDtos,
                new TypeToken<List<SearchRoomResponseItem>>() {
                }.getType());

        SearchRoomResponse returnValue = new SearchRoomResponse();
        returnValue.setRooms(items);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Search room is successful");
        return returnValue;
    }


    @GetMapping("/{publicId}")
    public RoomResponse viewRoom(@PathVariable String publicId) {
        log.info("viewRoom publicId:" + publicId);
        if (!roomService.isOrgAdminAccessingRoom(publicId) && !roomService.isRoomAdminAccessingRoom(publicId) && !roomService.isUserAccessingRoom(publicId)) {
            throw new AccessDeniedException("You are not allowed to view room " + publicId);
        }
        RoomDto roomDto = roomService.viewRoom(publicId);
        RoomResponse returnValue = new RoomResponse();
        new ModelMapper().map(roomDto, returnValue);

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("View room is successful");

        return returnValue;
    }

    @GetMapping("/{publicId}/availableTimeslots/{date}")
    public AvailableTimeslotResponse listAvailableTimeslots(@PathVariable("publicId") String roomPublicId,
                                                            @PathVariable String date) {
        List<AvailableTimeslotDto> availableTimeslotDtos = roomService.listAvailableTimeslots(roomPublicId, date);

        AvailableTimeslotResponse returnValue = new AvailableTimeslotResponse();
        return returnValue;
    }

    @GetMapping("/{publicId}/users")
    public SearchUserResponse listRoomUsers(@PathVariable("publicId") String roomPublicId) {
        List<UserDto> userDtos = roomService.listRoomUsers(roomPublicId);
        List<UserResponse> userResponses = new ModelMapper().map(userDtos, new TypeToken<ArrayList<UserResponse>>() {
        }.getType());
        SearchUserResponse returnValue = new SearchUserResponse();
        returnValue.setUsers(userResponses);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }

    @GetMapping("/{publicId}/admins")
    public SearchUserResponse listRoomAdmins(@PathVariable("publicId") String roomPublicId) {
        List<UserDto> userDtos = roomService.listRoomAdmins(roomPublicId);
        List<UserResponse> userResponses = new ModelMapper().map(userDtos, new TypeToken<ArrayList<UserResponse>>() {
        }.getType());
        SearchUserResponse returnValue = new SearchUserResponse();
        returnValue.setUsers(userResponses);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }

    @GetMapping("/{publicId}/bookings")
    public SearchBookingResponse listRoomBookings(@PathVariable("publicId") String roomPublicId) {
        List<BookingDto> bookingDtos = roomService.listRoomBookings(roomPublicId);
        List<SearchBookingResponseItem> bookings = new ModelMapper().map(bookingDtos,
                new TypeToken<ArrayList<SearchBookingResponseItem>>() {
                }.getType());
        SearchBookingResponse returnValue = new SearchBookingResponse();
        returnValue.setBookings(bookings);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }

    @PostMapping
    public GeneralResponse createRoom(
            @RequestPart(value = "roomImages", required = false) MultipartFile[] roomImages,
            @RequestPart(value = "roomData") RoomRequest request
    ) throws IOException {
        log.info("createRoom " + request.toString());
        roomService.createRoom(request, roomImages);
        return generalSuccessResponse("Create room is successful");
    }

    @PatchMapping
    public RoomResponse updateRoom(@RequestBody RoomRequest request) {
        log.info("updateRoom " + request.toString());
        String publicId = request.getPublicId();
        if (!roomService.isAccessingMyRoomOrgAdmin(publicId)) {
            throw new AccessDeniedException("You are not allowed to edit this room");
        }
        String name = request.getName();
        Integer capacity = request.getCapacity();
        String facilities = request.getFacilities();
        String description = request.getDescription();
        String organization = request.getOrgPublicId();
        Boolean active = request.isActive();
        List<String> admins = request.getAdmins();
        List<String> users = request.getUsers();
        List<BlockedTimeSlot> blockedTimeslots = request.getBlockedTimeslots();

        RoomDto roomDto = roomService.updateRoom(publicId, name, capacity, facilities, description, active, organization,
                admins, users, blockedTimeslots);

        RoomResponse returnValue = new RoomResponse();
        new ModelMapper().map(roomDto, returnValue);

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Update room is successful");
        return returnValue;
    }

    @PatchMapping(path = "/{roomId}/users")
    public RoomUserResponse enrollUser(@PathVariable String roomId, @RequestBody RoomUserRequest request) {
        log.info("enrollUser roomId:" + roomId + "," + request.toString());

        if (!roomService.isOrgAdminAccessingRoom(roomId) && !roomService.isRoomAdminAccessingRoom(roomId)) {
            throw new AccessDeniedException("You are not allowed to enroll user for this room");
        }

        RoomUserResponse returnValue = new RoomUserResponse();
        String action = MyStringUtils.toUpperCaseAndTrim(request.getAction());
        List<UserDto> userDtos = new ArrayList<UserDto>();
        switch (action) {
            case "list":
                userDtos = roomService.listEnrolledUsers(roomId);
                populateRoomUserResponse(returnValue, userDtos, "List room's user is successful");
                return returnValue;
            case "enroll":
                userDtos = roomService.enrollUser(roomId, request.getUsers());
                populateRoomUserResponse(returnValue, userDtos, "Enroll user in room is successful");
                return returnValue;
            case "unenroll":
                userDtos = roomService.unenrollUser(roomId, request.getUsers());
                populateRoomUserResponse(returnValue, userDtos, "Unenroll user from room is successful");
                return returnValue;
            default:
                throw new InvalidInputException("Invalid action:" + action);
        }
    }

    public void populateRoomUserResponse(RoomUserResponse returnValue, List<UserDto> userDtos, String s) {
        if (userDtos != null && userDtos.size() > 0) {
            for (UserDto userDto : userDtos) {
                returnValue.getUsers().add(userDto.getPublicId());
            }
        }
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage(s);
    }

    @DeleteMapping(path = "/{roomId}")
    public GeneralResponse deleteRoom(@PathVariable String roomId) {
        log.info("deleteRoom roomId:" + roomId);

        if (!roomService.isAccessingMyRoomOrgAdmin(roomId)) {
            throw new AccessDeniedException("You are not allowed to delete this room id:" + roomId);
        }

        roomService.deleteRoom(roomId);

        GeneralResponse returnValue = new GeneralResponse();

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Delete room is successful");
        return returnValue;
    }


}
