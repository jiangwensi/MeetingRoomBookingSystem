package com.jiangwensi.mrbs.utils;

import com.jiangwensi.mrbs.dto.*;
import com.jiangwensi.mrbs.entity.*;
import com.jiangwensi.mrbs.model.response.organization.OrganizationResponse;
import com.jiangwensi.mrbs.model.response.organization.SearchOrganizationResponseItem;
import com.jiangwensi.mrbs.model.response.room.RoomResponse;
import com.jiangwensi.mrbs.model.response.user.UserResponse;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jiang Wensi on 23/8/2020
 */
public class MyModelMapper {

    public static ModelMapper userEntityToUserDtoModelMapper() {

        Converter<UserEntity, UserDto> converter = new Converter<UserEntity, UserDto>() {
            @Override
            public UserDto convert(MappingContext<UserEntity, UserDto> context) {
                UserEntity entity = context.getSource();
                UserDto dto = context.getDestination();
                dto.setEmail(entity.getEmail());
                dto.setName(entity.getName());
                dto.setPublicId(entity.getPublicId());
                dto.setActive(entity.isActive());
                dto.setEmailVerified(entity.isEmailVerified());
                dto.setIsAdminOfOrganizations(entity.getIsAdminOfOrganizations().stream().map(e -> e.getPublicId()).collect(Collectors.toList()));
                dto.setIsAdminOfRooms(entity.getIsAdminOfRooms().stream().map(e -> e.getName()).collect(Collectors.toList()));
                dto.setRoles(entity.getRoles().stream().map(e -> e.getName()).collect(Collectors.toList()));
                dto.setTokens(entity.getTokens().stream().map(e->e.getToken()).collect(Collectors.toList()));
                return dto;
            }
        };

        ModelMapper mm = new ModelMapper();
        mm.addConverter(converter);
        return mm;
    }

    public static ModelMapper userDtoToUserResponseModelMapper(UserResponse old) {

        Converter<UserDto, UserResponse> converter = new Converter<UserDto, UserResponse>() {
            @Override
            public UserResponse convert(MappingContext<UserDto, UserResponse> context) {
                UserDto userDto = context.getSource();
                UserResponse userResponse = context.getDestination();

                userResponse.setEmail(MyStringUtils.isEmpty(userDto.getEmail()) ? old.getEmail() : userDto.getEmail());
                userResponse.setName(MyStringUtils.isEmpty(userDto.getName()) ? old.getName() : userDto.getName());
                userResponse.setPublicId(MyStringUtils.isEmpty(userDto.getPublicId()) ? old.getPublicId() : userDto.getPublicId());
                userResponse.setActive(userDto.isActive());
                userResponse.setEmailVerified(userDto.isEmailVerified());
                userResponse.setIsAdminOfOrganizations(userDto.getIsAdminOfOrganizations() == null ?
                        old.getIsAdminOfOrganizations() : userDto.getIsAdminOfOrganizations());
                userResponse.setIsAdminOfRooms(userDto.getIsAdminOfRooms() == null ? old.getIsAdminOfRooms() : userDto.getIsAdminOfRooms());
                userResponse.setRoles(userDto.getRoles() == null ? old.getRoles() : userDto.getRoles());

                if (!StringUtils.isEmpty(old.getErrorMessage())) {
                    userResponse.setErrorMessage(old.getErrorMessage());
                }
                if (!StringUtils.isEmpty(old.getMessage())) {
                    userResponse.setErrorMessage(old.getMessage());
                }
                if (!StringUtils.isEmpty(old.getStatus())) {
                    userResponse.setErrorMessage(old.getStatus());
                }

                return userResponse;
            }
        };

        ModelMapper mm = new ModelMapper();
        mm.addConverter(converter);
        return mm;
    }

    public static ModelMapper organizationEntityToOrganizationDtoModelMapper() {
        Converter<OrganizationEntity, OrganizationDto> converter = new Converter<OrganizationEntity, OrganizationDto>() {
            @Override
            public OrganizationDto convert(MappingContext<OrganizationEntity, OrganizationDto> context) {
                OrganizationEntity s = context.getSource();
                OrganizationDto d = context.getDestination();

                d.setPublicId(s.getPublicId());
                d.setName(s.getName());
                d.setActive(s.isActive());
                d.setDescription(s.getDescription());

                List<UserEntity> admins = s.getAdmins();
                if (admins != null && admins.size() > 0) {
                    List<String> adminStrs = new ArrayList<String>();
                    admins.forEach(a -> adminStrs.add(a.getPublicId()));
                    d.setAdmins(adminStrs);
                }

                List<RoomEntity> rooms = s.getRooms();
                if (rooms != null && rooms.size() > 0) {
                    List<String> roomStrs = new ArrayList<String>();
                    rooms.forEach(r -> roomStrs.add(r.getPublicId()));
                    d.setRooms(roomStrs);
                }

                return d;
            }
        };
        ModelMapper mm = new ModelMapper();
        mm.addConverter(converter);
        return mm;
    }

    public static ModelMapper organizationDtoToOrganizationSearchResponseItemModelMapper() {
        Converter<OrganizationDto, SearchOrganizationResponseItem> converter =
                new Converter<OrganizationDto, SearchOrganizationResponseItem>() {
                    @Override
                    public SearchOrganizationResponseItem convert(MappingContext<OrganizationDto, SearchOrganizationResponseItem> context) {
                        OrganizationDto s = context.getSource();
                        SearchOrganizationResponseItem d = context.getDestination();

                        d.setPublicId(s.getPublicId());
                        d.setName(s.getName());
                        d.setActive(s.isActive());
                        d.setDescription(s.getDescription());
                        d.setAdmins(s.getAdmins());
                        d.setRooms(s.getRooms());

                        return d;
                    }
                };
        ModelMapper mm = new ModelMapper();
        mm.addConverter(converter);
        return mm;
    }

    public static ModelMapper organizatioDtoToOrganizationResponseModelMapper() {
        Converter<OrganizationDto, OrganizationResponse> converter =
                new Converter<OrganizationDto, OrganizationResponse>() {
            @Override
            public OrganizationResponse convert(MappingContext<OrganizationDto, OrganizationResponse> context) {
                OrganizationDto s = context.getSource();
                OrganizationResponse d = context.getDestination();

                d.setPublicId(s.getPublicId());
                d.setName(s.getName());
                d.setActive(s.isActive());
                d.setDescription(s.getDescription());
                d.setAdmins(s.getAdmins());
                d.setRooms(s.getRooms());

                return d;
            }
        };
        ModelMapper mm = new ModelMapper();
        mm.addConverter(converter);
        return mm;
    }

    public static ModelMapper roomEntityToDtoModelMapper() {
        Converter<RoomEntity, RoomDto> converter = new Converter<RoomEntity, RoomDto>() {
            @Override
            public RoomDto convert(MappingContext<RoomEntity, RoomDto> context) {
                RoomEntity s = context.getSource();
                RoomDto d = context.getDestination();
                d.setActive(s.isActive());
                d.setName(s.getName());
                d.setCapacity(s.getCapacity());
                d.setDescription(s.getDescription());
                d.setFacilities(s.getFacilities());
                d.setOrganization(s.getOrganization().getPublicId());
                d.setPublicId(s.getPublicId());

                List<String> bookings = new ArrayList<>();
                List<BookingEntity> bookingEntities = s.getBookings();
                if(bookingEntities!=null){
                    bookingEntities.forEach(e->bookings.add(e.getPublicId()));
                };
                d.setBookings(bookings);

                List<String> admins = new ArrayList<>();
                List<UserEntity> adminEntities = s.getAdmins();
                if(adminEntities!=null){
                    adminEntities.forEach(e->admins.add(e.getPublicId()));
                }
                d.setAdmins(admins);

                List<String> users = new ArrayList<>();
                List<UserEntity> userEntities = s.getUsers();
                if(userEntities!=null){
                    userEntities.forEach(e->users.add(e.getPublicId()));
                }
                d.setUsers(users);

                List<BlockedTimeslotEntity> blockTimeslotEntities = s.getBlockedTimeslots();
                List<BlockedTimeslotDto> blockedTimeslotDtos = new ArrayList<>();
                if(blockTimeslotEntities!=null){
                    for(BlockedTimeslotEntity be: blockTimeslotEntities){
                        BlockedTimeslotDto dto = new BlockedTimeslotDto();
                        dto.setDay(be.getDay());
                        dto.setTimeFrom(be.getTimeFrom());
                        dto.setTimeTo(be.getTimeTo());
                        dto.setType(be.getType());
                        blockedTimeslotDtos.add(dto);
                    }
                }
                d.setBlockedTimeslots(blockedTimeslotDtos);

                List<RoomImageEntity> roomImageEntities = s.getRoomImages();
                List<RoomImageDto> roomImageDtos = new ArrayList<>();
                if(roomImageEntities!=null){
                    for(RoomImageEntity e: roomImageEntities){
                        RoomImageDto dto = new RoomImageDto();
                        System.out.println(e.getImage());
                        System.out.println(Base64.getEncoder().encodeToString(e.getImage()));
                        dto.setImage(Base64.getEncoder().encodeToString(e.getImage()));
                        dto.setId(e.getPublicId());
                        roomImageDtos.add(dto);
                    }
                }
                d.setRoomImages(roomImageDtos);

                return d;
            }
        };

        ModelMapper mm = new ModelMapper();
        mm.addConverter(converter);
        return mm;
    }

    public static ModelMapper bookingEntityToDtoModelMapper() {
        Converter<BookingEntity, BookingDto> converter = new Converter<BookingEntity, BookingDto>() {
            @Override
            public BookingDto convert(MappingContext<BookingEntity, BookingDto> context) {
                BookingEntity s = context.getSource();
                BookingDto d = context.getDestination();
                d.setBookedBy(s.getBookedBy().getPublicId());
                d.setFromTime(s.getFromTime());
                d.setToTime(s.getToTime());
                d.setPublicId(s.getPublicId());
                d.setRoomId(s.getRoom().getPublicId());
                return d;
            }
        };
        ModelMapper mm = new ModelMapper();
        mm.addConverter(converter);
        return mm;
    }

    public static ModelMapper roomDtoToRoomResponseMapper() {
        Converter<RoomDto, RoomResponse> converter = new Converter<RoomDto,RoomResponse>() {
            @Override
            public RoomResponse convert(MappingContext<RoomDto, RoomResponse> contex) {
                RoomDto s = contex.getSource();
                RoomResponse d = contex.getDestination();
                d.setOrganization(s.getOrganization());
                d.setFacilities(s.getFacilities());
                d.setCapacity(s.getCapacity());
                d.setBookings(s.getBookings());
                d.setActive(s.isActive());
                d.setAdmins(s.getAdmins());
                d.setDescription(s.getDescription());
                d.setImages(s.getRoomImages().stream().map(e->e.getImage()).collect(Collectors.toList()));
                d.setName(s.getName());
                d.setPublicId(s.getPublicId());
                d.setUsers(s.getUsers());
                return d;
            }
        };
        ModelMapper mm = new ModelMapper();
        mm.addConverter(converter);
        return mm;
    }
}


