package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.*;
import com.jiangwensi.mrbs.entity.*;
import com.jiangwensi.mrbs.exception.AccessDeniedException;
import com.jiangwensi.mrbs.exception.InvalidInputException;
import com.jiangwensi.mrbs.exception.NotFoundException;
import com.jiangwensi.mrbs.model.request.room.BlockedTimeSlot;
import com.jiangwensi.mrbs.model.request.room.RoomRequest;
import com.jiangwensi.mrbs.repo.*;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OrgRepository orgRepo;

    @Autowired
    private BlockTimeslotRepo blockTimeslotRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    OrgService orgService;

    @Override
    public List<RoomDto> searchRoom(String name, String orgName, Boolean active) {
        log.info("searchRoom name:" + name + ",active:" + active);

        List<RoomDto> returnValue = new ArrayList<>();
//        List<RoomEntity> roomEntities = roomRepo.searchRoom(name, active);
        List<RoomEntity> roomEntities = roomRepo.searchRoom(name, orgName, active);

        if (roomEntities == null) {
            throw new NotFoundException("Unable to find room by name:" + name + ",active=" + active);
        }

        ModelMapper mm = MyModelMapper.roomEntityToDtoModelMapper();

        roomEntities
                .stream()
                .filter(e ->
                        isOrgAdminAccessingRoom(e.getPublicId()) ||
                        isRoomAdminAccessingRoom(e.getPublicId()) ||
                        isUserAccessingRoom(e.getPublicId()))
                .forEach(e -> {
                    RoomDto roomDto = new RoomDto();
                    mm.map(e, roomDto);
                    returnValue.add(roomDto);
        });

        return returnValue;
    }

    @Override
    public RoomDto viewRoom(String publicId) {
        log.info("viewRoom publicId:" + publicId);

        RoomDto returnValue = new RoomDto();
        RoomEntity roomEntity = roomRepo.findByPublicId(publicId);

        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by publicId:" + publicId);
        }

        ModelMapper mm = MyModelMapper.roomEntityToDtoModelMapper();
        mm.map(roomEntity, returnValue);

        return returnValue;
    }

    @Override
    @Transactional
    public RoomDto createRoom(RoomRequest request, MultipartFile[] roomImages) throws IOException {


        String name = request.getName();
        Integer capacity = request.getCapacity();
        String facilities = request.getFacilities();
        String description = request.getDescription();
        String organization = request.getOrgPublicId();
        Boolean active = request.isActive();
        List<String> admins = request.getAdmins();
        List<BlockedTimeSlot> blockedTimeslots = request.getBlockedTimeslots();

        if (!userService.isAccessingMyOrg(organization)) {
            throw new AccessDeniedException("You are not allowed to create room for this organization " + organization);
        }

        if (roomRepo.findByRoomNameAndOrgPublicId(name, organization) != null) {
            throw new InvalidInputException("There is an existing room with the same name in this organization. " +
                    "Please try a different name");
        }
        RoomDto returnValue = new RoomDto();
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setName(name);
        roomEntity.setCapacity(capacity);
        roomEntity.setFacilities(facilities);
        roomEntity.setDescription(description);
        roomEntity.setActive(active);

        if (admins != null) {
            List<UserEntity> userEntities = new ArrayList<>();
            admins.forEach(e -> {
                UserEntity userEntity = userRepo.findByEmail(e);
                if (userEntity == null) {
                    throw new NotFoundException("Unable to find user by id:" + e);
                }
                userEntities.add(userEntity);
            });
            roomEntity.setAdmins(userEntities);
        }

        if (organization != null) {
            OrganizationEntity organizationEntity = orgRepo.findByPublicId(organization);
            if (organizationEntity == null) {
                throw new NotFoundException("Unable to find organiztion by id:" + organization);
            }

            roomEntity.setOrganization(organizationEntity);
        }

        if (roomImages != null && roomImages.length > 0) {
            List<RoomImageEntity> roomImageEntities = new ArrayList<RoomImageEntity>();
            for (int i = 0; i < roomImages.length; i++) {
                RoomImageEntity e = new RoomImageEntity();
                e.setImage(ArrayUtils.toObject(roomImages[i].getBytes()));
                e.setRoom(roomEntity);
                roomImageEntities.add(e);
            }
            roomEntity.setRoomImages(roomImageEntities);
        }

        List<BlockedTimeslotEntity> blockedTimeSlots = new ArrayList<>();
        if (blockedTimeslots != null && blockedTimeslots.size() > 0) {
            for (BlockedTimeSlot b : blockedTimeslots) {
                BlockedTimeslotEntity e = new BlockedTimeslotEntity();
                new ModelMapper().map(b, e);
                e.setRoom(roomEntity);
                blockedTimeSlots.add(e);
            }
        }
        roomEntity.setBlockedTimeslots(blockedTimeSlots);

        roomEntity = roomRepo.save(roomEntity);

        ModelMapper mm = MyModelMapper.roomEntityToDtoModelMapper();
        mm.map(roomEntity, returnValue);

        return returnValue;
    }


    @Override
    @Transactional
    public RoomDto updateRoom(String publicId, String name, Integer capacity, String facilities, String description,
                              Boolean active,
                              String organization,
                              List<String> admins,
                              List<String> users, List<BlockedTimeSlot> blockedTimeslots) {
        log.info("updateRoom publicId:" + publicId + ",name:" + name + ",capacity:" + capacity + ",facilities:" + facilities + "," +
                "description:" + description + ",active:" + active + ",admins:" + admins == null ? "" : String.join(" ", admins));

        RoomEntity roomEntity = roomRepo.findByPublicId(publicId);

        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by id:" + publicId);
        }

        if (!MyStringUtils.isEmpty(name)) {
            roomEntity.setName(name);
        }

        if (capacity != null) {
            roomEntity.setCapacity(capacity);
        }

        if (!MyStringUtils.isEmpty(facilities)) {
            roomEntity.setFacilities(facilities);
        }

        if (!MyStringUtils.isEmpty(description)) {
            roomEntity.setDescription(description);
        }

        if (active != null) {
            roomEntity.setActive(active);
        }

        if (!MyStringUtils.isEmpty(organization)) {
            OrganizationEntity orgEntity = orgRepo.findByPublicId(organization);
            if (orgEntity == null) {
                throw new NotFoundException("Unable to find organization by id:" + organization);
            }
            roomEntity.setOrganization(orgEntity);
        }

        if (admins != null & admins.size() > 0) {
            List<UserEntity> userEntities = new ArrayList<UserEntity>();
            admins.forEach(e -> {
                UserEntity userEntity = userRepo.findByPublicId(e);
                if (userEntity == null) {
                    throw new NotFoundException("Unable to find user by id:" + e);
                }
                userEntities.add(userEntity);
            });
            roomEntity.setAdmins(userEntities);
        }

        if (users != null) {
            List<UserEntity> userEntities = new ArrayList<UserEntity>();
            users.forEach(e -> {
                UserEntity userEntity = userRepo.findByPublicId(e);
                if (userEntity == null) {
                    throw new NotFoundException("Unable to find user by id:" + e);
                }
                userEntities.add(userEntity);
            });
            roomEntity.setUsers(userEntities);
        }

        roomEntity = roomRepo.save(roomEntity);

        if (blockedTimeslots != null) {
            //remove old slots
            blockTimeslotRepo.deleteAll(roomEntity.getBlockedTimeslots());

            //add updated slots
            for (BlockedTimeSlot b : blockedTimeslots) {
                BlockedTimeslotEntity e = new BlockedTimeslotEntity();
                new ModelMapper().map(b, e);
                e.setRoom(roomEntity);
                blockTimeslotRepo.save(e);
            }
        }

        RoomDto returnValue = new RoomDto();
        ModelMapper mm = MyModelMapper.roomEntityToDtoModelMapper();
        mm.map(roomEntity, returnValue);

        return returnValue;
    }

    @Override
    @Transactional
    public void deleteRoom(String publicId) {
        log.info("deleteRoom publicId:" + publicId);

        RoomEntity roomEntity = roomRepo.findByPublicId(publicId);
        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by id:" + publicId);
        }
        List<BlockedTimeslotEntity> slots = roomEntity.getBlockedTimeslots();
        if (slots != null) {
            slots.forEach(b -> blockTimeslotRepo.delete(b));
        }
        roomRepo.delete(roomEntity);
    }

    @Override
    public List<UserDto> enrollUser(String roomPublicId, List<String> users) {
        log.info("enrollUser roomPublicId:" + roomPublicId + ",users:" + users == null ? null : String.join(" ", users));

        RoomEntity roomEntity = roomRepo.findByPublicId(roomPublicId);
        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by id:" + roomPublicId);
        }

        if (users != null && users.size() > 0) {
            users.forEach(e -> {
                UserEntity userEntity = userRepo.findByPublicId(e);
                if (userEntity == null) {
                    throw new NotFoundException("Unable to find user by id:" + e);
                }
                roomEntity.getUsers().add(userEntity);
            });
        }

        roomRepo.save(roomEntity);

        return listEnrolledUsers(roomPublicId);

    }

    @Override
    public List<UserDto> unenrollUser(String roomPublicId, List<String> users) {
        log.info("unenrollUser roomPublicId:" + roomPublicId + ",users:" + users == null ? null : String.join(" ", users));

        RoomEntity roomEntity = roomRepo.findByPublicId(roomPublicId);
        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by id:" + roomPublicId);
        }

        if (users != null && users.size() > 0) {
            users.forEach(e -> {
                UserEntity userEntity = userRepo.findByPublicId(e);
                if (userEntity == null) {
                    throw new NotFoundException("Unable to find user by id:" + e);
                }
                roomEntity.getUsers().remove(userEntity);
            });
        }

        roomRepo.save(roomEntity);

        return listEnrolledUsers(roomPublicId);

    }

    @Override
    public List<UserDto> listEnrolledUsers(String roomId) {
        log.info("listEnrolledUsers roomId:" + roomId);

        List<UserDto> returnValue = new ArrayList<>();

        List<UserEntity> userEntities = userRepo.findByRoom(roomId);

        if (userEntities != null && userEntities.size() > 0) {

            userEntities.forEach(e -> {
                UserDto userDto = new UserDto();
                MyModelMapper.userEntityToUserDtoModelMapper().map(e, userDto);
                returnValue.add(userDto);
            });

        }
        return returnValue;
    }

    @Override
    public List<UserDto> listRoomUsers(String roomPublicId) {
        RoomEntity roomEntity = roomRepo.findByPublicId(roomPublicId);

        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by id " + roomPublicId);
        }

        List<UserEntity> users = roomEntity.getUsers();

        if (users == null) {
            throw new NotFoundException("There is no users in room " + roomPublicId);
        }

        List<UserDto> returnValue = new ArrayList<>();

        for (UserEntity ue : users) {
            UserDto userDto = new UserDto();
            MyModelMapper.userEntityToUserDtoModelMapper().map(ue, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public List<UserDto> listRoomAdmins(String roomPublicId) {
        RoomEntity roomEntity = roomRepo.findByPublicId(roomPublicId);

        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by id " + roomPublicId);
        }

        List<UserEntity> admins = roomEntity.getAdmins();

        if (admins == null) {
            throw new NotFoundException("There is no admins in room " + roomPublicId);
        }

        List<UserDto> returnValue = new ArrayList<>();

        for (UserEntity ue : admins) {
            UserDto userDto = new UserDto();
            MyModelMapper.userEntityToUserDtoModelMapper().map(ue, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public List<BookingDto> listRoomBookings(String roomPublicId) {
        RoomEntity roomEntity = roomRepo.findByPublicId(roomPublicId);

        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by id " + roomPublicId);
        }

        List<BookingEntity> bookings = roomEntity.getBookings();

        if (bookings == null) {
            throw new NotFoundException("There is no bookings in room " + roomPublicId);
        }

        List<BookingDto> retrunValue = new ArrayList<>();

        for (BookingEntity be : bookings) {
            BookingDto bookingDto = new BookingDto();
            MyModelMapper.bookingEntityToDtoModelMapper().map(be, bookingDto);
            retrunValue.add(bookingDto);
        }

        return retrunValue;
    }

    @Override
    public List<AvailableTimeslotDto> listAvailableTimeslots(String roomPublicId, String date) {
        //TODO
        //Apply procedure here
        return null;
    }


    @Override
    public boolean isAccessingMyOrg(String orgPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        List<String> isAdminOfOrganizations = userDto.getIsAdminOfOrganizations();
        if (isAdminOfOrganizations != null) {
            return isAdminOfOrganizations.contains(orgPublicId);
        }
        return false;
    }


    @Override
    public boolean isAccessingMyRoomOrgAdmin(String roomPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        RoomDto roomDto = viewRoom(roomPublicId);
        UserDto userDto = userService.findUserByEmail(auth.getName());
        OrganizationDto organizationDto = orgService.viewOrganization(roomDto.getOrganization());

        List<String> orgAdmins = organizationDto.getAdmins();
        for (String admin : orgAdmins) {
            if (userDto.getPublicId().equals(admin)) {
                log.info("isAccessingMyRoomOrgAdmin=true");
                return true;
            }
        }
        log.info("isAccessingMyRoomOrgAdmin=false");
        return false;
    }

    @Override
    public boolean isOrgAdminAccessingRoom(String roomPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());

        List<String> roomsInMyOrganizations = new ArrayList<>();
        List<String> isAdminOfOrganizations = userDto.getIsAdminOfOrganizations();
        if (isAdminOfOrganizations != null) {
            List<OrganizationDto> organizationDtos =
                    isAdminOfOrganizations.stream().map(e -> orgService.viewOrganization(e)).collect(Collectors.toList());
            if (organizationDtos != null) {
                organizationDtos.forEach(e -> roomsInMyOrganizations.addAll(e.getRooms()));
            }
        }

        return roomsInMyOrganizations.contains(roomPublicId);
    }

    @Override
    public boolean isRoomAdminAccessingRoom(String roomPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        List<String> isAdminOfRooms = userDto.getIsAdminOfRooms();
        if (isAdminOfRooms != null) {
            return isAdminOfRooms.contains(roomPublicId);
        }
        return false;
    }

    @Override
    public boolean isUserAccessingRoom(String roomPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return false;
    }
}

















