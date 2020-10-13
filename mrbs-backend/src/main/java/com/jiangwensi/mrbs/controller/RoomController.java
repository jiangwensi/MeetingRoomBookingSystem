package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.dto.*;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
@RestController
@Slf4j
@RequestMapping(PathConst.ROOM_PATH)
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Scope("request")
public class RoomController {
    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    @Autowired
    OrgService orgService;

    @GetMapping
    public SearchRoomResponse search(@RequestParam(required = false,value="roomName") String name,
                                     @RequestParam(required = false,value="orgName") String orgName,
                                     @RequestParam(required = false) Boolean active) {
        log.info("search name:" + name + ",active:" + active);

        List<RoomDto> roomDtos = roomService.searchRoom(name, orgName, active);

        roomDtos.stream().filter(e-> isOrgAdminAccessingRoom(e.getPublicId())||isRoomAdminAccessingRoom(e.getPublicId())|| isUserAccessingRoom(e.getPublicId()));

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
        if(!isOrgAdminAccessingRoom(publicId) && !isRoomAdminAccessingRoom(publicId) && !isUserAccessingRoom(publicId)){
            throw new AccessDeniedException("You are not allowed to view room "+publicId);
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
                                                            @PathVariable String date){
        List<AvailableTimeslotDto> availableTimeslotDtos = roomService.listAvailableTimeslots(roomPublicId,date);

        AvailableTimeslotResponse returnValue = new AvailableTimeslotResponse();
        return returnValue;
    }

    @GetMapping("/{publicId}/users")
    public SearchUserResponse listRoomUsers(@PathVariable("publicId") String roomPublicId){
        List<UserDto> userDtos = roomService.listRoomUsers(roomPublicId);
        List<UserResponse> userResponses = new ModelMapper().map(userDtos,new TypeToken<ArrayList<UserResponse>>(){}.getType());
        SearchUserResponse returnValue = new SearchUserResponse();
        returnValue.setUsers(userResponses);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }

    @GetMapping("/{publicId}/admins")
    public SearchUserResponse listRoomAdmins(@PathVariable("publicId") String roomPublicId){
        List<UserDto> userDtos = roomService.listRoomAdmins(roomPublicId);
        List<UserResponse> userResponses = new ModelMapper().map(userDtos,new TypeToken<ArrayList<UserResponse>>(){}.getType());
        SearchUserResponse returnValue = new SearchUserResponse();
        returnValue.setUsers(userResponses);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }

    @GetMapping("/{publicId}/bookings")
    public SearchBookingResponse listRoomBookings(@PathVariable("publicId") String roomPublicId){
        List<BookingDto> bookingDtos = roomService.listRoomBookings(roomPublicId);
        List<SearchBookingResponseItem> bookings = new ModelMapper().map(bookingDtos,
                new TypeToken<ArrayList<SearchBookingResponseItem>>(){}.getType());
        SearchBookingResponse returnValue = new SearchBookingResponse();
        returnValue.setBookings(bookings);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }

    @PostMapping
    public RoomResponse createRoom(@RequestBody RoomRequest request) {
        log.info("createRoom "+request.toString());
        String name = request.getName();
        Integer capacity = request.getCapacity();
        String facilities = request.getFacilities();
        String description = request.getDescription();
        String organization = request.getOrgPublicId();
        Boolean active = request.isActive();
        List<String> admins = request.getAdmins();
        List<BlockedTimeSlot> blockedTimeslots = request.getBlockedTimeslots();

        if(!isAccessingMyOrg(organization)){
            throw new AccessDeniedException("You are not allowed to create room for organization "+organization);
        }

        RoomDto roomDto = roomService.createRoom(name,capacity,facilities,description,active,organization,admins,
                blockedTimeslots);

        RoomResponse returnValue = new RoomResponse();
        new ModelMapper().map(roomDto, returnValue);

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Create room is successful");
        return returnValue;
    }

    @PatchMapping
    public RoomResponse updateRoom(@RequestBody RoomRequest request) {
        log.info("updateRoom "+request.toString());
        String publicId = request.getPublicId();
        if(!isAccessingMyRoomOrgAdmin(publicId)){
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

        RoomDto roomDto = roomService.updateRoom(publicId,name,capacity,facilities,description,active,organization,
                admins,users,blockedTimeslots);

        RoomResponse returnValue = new RoomResponse();
        new ModelMapper().map(roomDto, returnValue);

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Update room is successful");
        return returnValue;
    }

    @PatchMapping(path="/{roomId}/users")
    public RoomUserResponse enrollUser(@PathVariable String roomId, @RequestBody RoomUserRequest request) {
        log.info("enrollUser roomId:"+roomId+","+request.toString());

        if(!isOrgAdminAccessingRoom(roomId) && !isRoomAdminAccessingRoom(roomId)){
            throw new AccessDeniedException("You are not allowed to enroll user for this room");
        }

        RoomUserResponse returnValue = new RoomUserResponse();
        String action = MyStringUtils.toUpperCaseAndTrim(request.getAction());
        List<UserDto> userDtos = new ArrayList<UserDto>();
        switch (action){
            case "list":
                userDtos = roomService.listEnrolledUsers(roomId);
                populateRoomUserResponse(returnValue, userDtos, "List room's user is successful");
                return returnValue;
            case "enroll":
                userDtos = roomService.enrollUser(roomId,request.getUsers());
                populateRoomUserResponse(returnValue, userDtos, "Enroll user in room is successful");
                return returnValue;
            case "unenroll":
                userDtos = roomService.unenrollUser(roomId,request.getUsers());
                populateRoomUserResponse(returnValue, userDtos, "Unenroll user from room is successful");
                return returnValue;
            default: throw new InvalidInputException("Invalid action:"+action);
        }
    }

    public void populateRoomUserResponse(RoomUserResponse returnValue, List<UserDto> userDtos, String s) {
        if(userDtos!=null && userDtos.size()>0){
            for (UserDto userDto : userDtos) {
                returnValue.getUsers().add(userDto.getPublicId());
            }
        }
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage(s);
    }

    @DeleteMapping(path="/{roomId}")
    public GeneralResponse deleteRoom(@PathVariable String roomId) {
        log.info("deleteRoom roomId:"+roomId);

        if(!isAccessingMyRoomOrgAdmin(roomId)){
            throw new AccessDeniedException("You are not allowed to delete this room id:"+roomId);
        }

        roomService.deleteRoom(roomId);

        GeneralResponse returnValue = new GeneralResponse();

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Delete room is successful");
        return returnValue;
    }


    private boolean isAccessingMyRoomOrgAdmin(String roomPublicId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        RoomDto roomDto = roomService.viewRoom(roomPublicId);
        UserDto userDto = userService.findUserByEmail(auth.getName());
        OrganizationDto organizationDto = orgService.viewOrganization(roomDto.getOrganization());

        List<String> orgAdmins = organizationDto.getAdmins();
        for(String admin: orgAdmins){
            if(userDto.getPublicId().equals(admin)){
                log.info("isAccessingMyRoomOrgAdmin=true");
                return true;
            }
        }
        log.info("isAccessingMyRoomOrgAdmin=false");
        return false;
    }

    private boolean isOrgAdminAccessingRoom(String roomPublicId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());

        List<String> roomsInMyOrganizations = new ArrayList<>();
        List<String> isAdminOfOrganizations = userDto.getIsAdminOfOrganizations();
        if(isAdminOfOrganizations!=null){
            List<OrganizationDto> organizationDtos =
            isAdminOfOrganizations.stream().map(e-> orgService.viewOrganization(e)).collect(Collectors.toList());
            if(organizationDtos!=null){
                organizationDtos.forEach(e->roomsInMyOrganizations.addAll(e.getRooms()));
            }
        }

        return roomsInMyOrganizations.contains(roomPublicId);
    }

    private boolean isAccessingMyOrg(String orgPublicId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        List<String> isAdminOfOrganizations = userDto.getIsAdminOfOrganizations();
        if(isAdminOfOrganizations!=null){
            return isAdminOfOrganizations.contains(orgPublicId);
        }
        return false;
    }

    private boolean isRoomAdminAccessingRoom(String roomPublicId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        List<String> isAdminOfRooms = userDto.getIsAdminOfRooms();
        if(isAdminOfRooms!=null){
            return isAdminOfRooms.contains(roomPublicId);
        }
        return false;
    }

    private boolean isUserAccessingRoom(String roomPublicId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return false;
    }

//    private boolean isCreatingMyRoomOrgAdmin(String organization){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDto userDto = userService.findUserByEmail(auth.getName());
//        OrganizationDto organizationDto = orgService.viewOrganization(organization);
//        List<String> orgAdmins = organizationDto.getAdmins();
//        for(String admin: orgAdmins){
//            if(userDto.getPublicId().equals(admin)){
//                log.info("isCreatingMyRoomOrgAdmin=true");
//                return true;
//            }
//        }
//        log.info("isCreatingMyRoomOrgAdmin=false");
//        return false;
//    }
//
//    private boolean isAccessingMyRoomRoomAdmin(String roomPublicId){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        UserDto userDto = userService.findUserByEmail(auth.getName());
//
//        RoomDto roomDto = roomService.viewRoom(roomPublicId);
//        List<String> roomAdmins = roomDto.getAdmins();
//        for(String admin: roomAdmins){
//            if(userDto.getPublicId().equals(admin)){
//                log.info("isAccessingMyRoomRoomAdmin=true");
//                return true;
//            }
//        }
//        log.info("isAccessingMyRoomRoomAdmin=false");
//        return false;
//    }
}
