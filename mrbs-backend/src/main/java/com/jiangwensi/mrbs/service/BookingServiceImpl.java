package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.dto.AvailableTimeslotDto;
import com.jiangwensi.mrbs.dto.BookingDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.entity.BookingEntity;
import com.jiangwensi.mrbs.entity.RoomEntity;
import com.jiangwensi.mrbs.entity.Slot;
import com.jiangwensi.mrbs.entity.UserEntity;
import com.jiangwensi.mrbs.exception.AccessDeniedException;
import com.jiangwensi.mrbs.exception.InvalidInputException;
import com.jiangwensi.mrbs.exception.NotFoundException;
import com.jiangwensi.mrbs.model.request.booking.BookingRequest;
import com.jiangwensi.mrbs.repo.BookingRepository;
import com.jiangwensi.mrbs.repo.RoomRepository;
import com.jiangwensi.mrbs.repo.UserRepository;
import com.jiangwensi.mrbs.utils.MyDateUtils;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepo;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public BookingServiceImpl(BookingRepository bookingRepo, UserService userService, UserRepository userRepository, RoomRepository roomRepository) {
        this.bookingRepo = bookingRepo;
        this.userService = userService;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public BookingDto createBooking(BookingRequest request) throws ParseException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());

        if (!userService.isAccessedByRoomUser(request.getRoomId()) && !userService.isAccessedByTargetRole("SYSADM")) {
            throw new AccessDeniedException("You are not allowed to book this room:" + request.getRoomId());
        }

        String timeformat = getTimeFormat();

        UserEntity userEntity = userRepository.findByPublicId(userDto.getPublicId());
        if (userEntity == null) {
            throw new NotFoundException("Unable to find user by id:" + userDto.getPublicId());
        }

        RoomEntity roomEntity = roomRepository.findByPublicId(request.getRoomId());
        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by id:" + request.getRoomId());
        }

        if (clash(request.getRoomId(), request.getFromTime(), request.getToTime())) {
            throw new InvalidInputException("The time slot is not available. roomId:" + request.getRoomId() + ",from:" + request.getFromTime() + ",to:" + request.getToTime());
        }

//        BookingEntity bookingEntity = new BookingEntity();
//        bookingEntity.setBookedBy(userEntity);
//        bookingEntity.setRoom(roomEntity);
//        bookingEntity.setDate(new SimpleDateFormat(timeformat).parse(request.getFromTime()));
//        bookingEntity.setFromTime(new SimpleDateFormat(timeformat).parse(request.getFromTime()));
//        bookingEntity.setToTime(new SimpleDateFormat(timeformat).parse(request.getToTime()));
//        bookingEntity.setPublicId(UUID.randomUUID().toString());
//        bookingEntity = bookingRepo.save(bookingEntity);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String from = sdf.format(sdf.parse(request.getFromTime()));
        String to = sdf.format(sdf.parse(request.getToTime()));
        String roomId = Long.toString(roomEntity.getId());
        String bookedBy = Long.toString(userEntity.getId());
        BookingEntity bookingEntity;
        try{
            bookingEntity = bookingRepo.createBooking(from,to,roomId,bookedBy);
        } catch (JpaSystemException e){
            throw new InvalidInputException("This selected time slot might be unavailable. Please try another " +
                    "timeslot.");
        }

        BookingDto returnValue = new BookingDto();
        MyModelMapper.bookingEntityToDtoModelMapper().map(bookingEntity, returnValue);
        return returnValue;
    }


    @Override
    public BookingDto updateBooking(BookingRequest request) throws ParseException {

        if (!userService.isAccessingMyBooking(request.getPublicId())
                && !userService.isAccessedByRoomAdmin(request.getPublicId())) {
            throw new AccessDeniedException("You are not allowed to update this booking:" + request.getPublicId());
        }

        BookingEntity bookingEntity = bookingRepo.findByPublicId(request.getPublicId());
        if (bookingEntity == null) {
            throw new NotFoundException("Unable to find booking id:" + request.getPublicId());
        }
        if (!MyStringUtils.isEmpty(request.getRoomId())) {
            RoomEntity roomEntity = roomRepository.findByPublicId(request.getRoomId());
            bookingEntity.setRoom(roomEntity);
        }
        if (!MyStringUtils.isEmpty(request.getFromTime())) {
            bookingEntity.setFromTime(new SimpleDateFormat(getTimeFormat()).parse(request.getFromTime()));
        }
        if (!MyStringUtils.isEmpty(request.getToTime())) {
            bookingEntity.setToTime(new SimpleDateFormat(getTimeFormat()).parse(request.getToTime()));
        }
        bookingEntity = bookingRepo.save(bookingEntity);
        BookingDto returnValue = new BookingDto();
        MyModelMapper.bookingEntityToDtoModelMapper().map(bookingEntity, returnValue);
        return returnValue;
    }

    @Override
    public BookingDto viewBooking(String bookingId) {

        if (!userService.isAccessingMyBooking(bookingId)
                && !userService.isAccessedByRoomAdmin(bookingId)
        ) {

            throw new AccessDeniedException("You are not allowed to view this booking:" + bookingId);

        }
        BookingEntity bookingEntity = bookingRepo.findByPublicId(bookingId);
        if (bookingEntity == null) {
            throw new NotFoundException("Unable to find booking id:" + bookingId);
        }
        BookingDto returnValue = new BookingDto();
        MyModelMapper.bookingEntityToDtoModelMapper().map(bookingEntity, returnValue);
        return returnValue;
    }

    @Override
    public void deleteBooking(String publicId) {


        if (!userService.isAccessingMyBooking(publicId) && !userService.isAccessedByRoomAdmin(publicId)) {
            throw new AccessDeniedException("You are not allowed to delete this booking:" + publicId);
        }
        BookingEntity bookingEntity = bookingRepo.findByPublicId(publicId);
        if (bookingEntity == null) {
            throw new NotFoundException("Unable to find booking id:" + publicId);
        }
        bookingRepo.delete(bookingEntity);
    }

    @Override
    public List<BookingDto> search(String roomPublicId, String date) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        boolean isSysAdm =
                auth.getAuthorities().contains(new SimpleGrantedAuthority("SYSADM"));


        if(date!=null && !date.equals("")){
            if(MyDateUtils.isValidFormat(date)) {
                throw new InvalidInputException("Invalid date format " + date);
            }
        }

        List<BookingEntity> bookingEntities;

        if(isSysAdm){
            bookingEntities = bookingRepo.searchBySysAdm(roomPublicId, date);
        } else {
            bookingEntities = bookingRepo.search(userDto.getPublicId(), roomPublicId, date);
        }

        List<BookingDto> returnValue = new ArrayList<>();
        for (BookingEntity bookingEntity : bookingEntities) {
            BookingDto bookingDto = new BookingDto();
            MyModelMapper.bookingEntityToDtoModelMapper().map(bookingEntity, bookingDto);
            returnValue.add(bookingDto);
        }
        return returnValue;
    }

    @Override
    public List<AvailableTimeslotDto> fetchAvailableslotByRoom(String roomId, String date) {
        if(date==null){
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        List<Slot> slots = bookingRepo.getAvailableSlots(roomId,date);
        List<AvailableTimeslotDto> returnResult = new ModelMapper().map(slots,
                new TypeToken<ArrayList<Slot>>(){}.getType());
        return returnResult;
    }

    public Boolean clash(String roomId, String from, String to) {
        List<BookingEntity> result = bookingRepo.checkClash(roomId, from, to);
        return result != null && result.size() > 0;
    }

    private String getTimeFormat() {
        return AppProperties.getAppProperties().getProperty("dateformat") + " " + AppProperties.getAppProperties().getProperty("timeformat");
    }
}
