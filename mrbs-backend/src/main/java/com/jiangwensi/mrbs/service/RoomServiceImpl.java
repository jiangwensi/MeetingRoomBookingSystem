package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.BookingDto;
import com.jiangwensi.mrbs.dto.RoomDto;
import com.jiangwensi.mrbs.dto.UserDto;
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

/**
 * Created by Jiang Wensi on 25/8/2020
 */
@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepo;
    private final UserRepository userRepo;
    private final OrgRepository orgRepo;
    private final BlockTimeslotRepo blockTimeslotRepo;
    private final BookingRepository bookingRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private OrgService orgService;

    public RoomServiceImpl(RoomRepository roomRepo, UserRepository userRepo, OrgRepository orgRepo, BlockTimeslotRepo blockTimeslotRepo, BookingRepository bookingRepo) {
        this.roomRepo = roomRepo;
        this.userRepo = userRepo;
        this.orgRepo = orgRepo;
        this.blockTimeslotRepo = blockTimeslotRepo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public List<RoomDto> searchRoom(String name, String orgName, Boolean active, Boolean myEnrolledRoomOnly) {

        List<RoomDto> returnValue = new ArrayList<>();
        List<RoomEntity> roomEntities = roomRepo.searchRoom(name, orgName, active);

        if (roomEntities == null) {
            throw new NotFoundException("Unable to find room by name:" + name + ",active=" + active);
        }

        ModelMapper mm = MyModelMapper.roomEntityToDtoModelMapper();

        roomEntities
                .stream()
                .filter(
                        e -> myEnrolledRoomOnly == null
                                || !myEnrolledRoomOnly
                                || (myEnrolledRoomOnly && userService.isAccessedByRoomUser(e.getPublicId()))
                                || (myEnrolledRoomOnly && userService.isRoomAdminAccessingRoom(e.getPublicId())))
                .forEach(e -> {
                    RoomDto roomDto = new RoomDto();
                    if (!userService.isOrgAdminAccessingRoom(e.getPublicId()) &&
                            !userService.isRoomAdminAccessingRoom(e.getPublicId()) &&
                            !userService.isSysadm()) {
                        e.setUsers(null);
                    }
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

        if (!userService.isOrgAdminAccessingRoom(publicId)
                && !userService.isRoomAdminAccessingRoom(publicId)
//                && !userService.isUserAccessingRoom(publicId)
                && !userService.isSysadm()) {
//            throw new AccessDeniedException("You are not allowed to view room " + publicId);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserEntity userEntity = userRepo.findByEmail(auth.getName());
            String myPublicId = userEntity.getPublicId();
            roomEntity.getUsers().stream().filter(e -> e.getPublicId().equals(myPublicId));
        }
        ModelMapper mm = MyModelMapper.roomEntityToDtoModelMapper();
        mm.map(roomEntity, returnValue);
        return returnValue;
    }

    @Override
    @Transactional
    public void createRoom(RoomRequest request, MultipartFile[] roomImages) throws IOException {


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
                e.setImage(roomImages[i].getBytes());
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

    }


    @Override
    @Transactional
    public RoomDto updateRoom(RoomRequest request) {
        String publicId = request.getPublicId();
        if (!userService.isAccessingMyRoomOrgAdmin(publicId)&&!userService.isRoomAdminAccessingRoom(publicId)) {
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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void deleteRoom(String publicId) {
        log.info("deleteRoom publicId:" + publicId);

        if (!userService.isAccessingMyRoomOrgAdmin(publicId) && !userService.isSysadm()) {
            throw new AccessDeniedException("You are not allowed to delete this room:" + publicId);
        }

        bookingRepo.deleteBookingByRoomPublicId(publicId);

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


        if (!userService.isOrgAdminAccessingRoom(roomPublicId) && !userService.isRoomAdminAccessingRoom(roomPublicId)) {
            throw new AccessDeniedException("You are not allowed to enroll user for this room");
        }
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


        if (!userService.isOrgAdminAccessingRoom(roomPublicId) && !userService.isRoomAdminAccessingRoom(roomPublicId)) {
            throw new AccessDeniedException("You are not allowed to enroll user for this room");
        }
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


        if (!userService.isOrgAdminAccessingRoom(roomId) && !userService.isRoomAdminAccessingRoom(roomId)) {
            throw new AccessDeniedException("You are not allowed to enroll user for this room");
        }

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


        if (!userService.isOrgAdminAccessingRoom(roomPublicId)
                && !userService.isRoomAdminAccessingRoom(roomPublicId)
//                && !userService.isUserAccessingRoom(publicId)
                && !userService.isSysadm()) {
            throw new AccessDeniedException("You are not allowed to list user for this room");
//            roomEntity.setUsers(null);
        }
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

}

















