package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.dto.AvailableTimeslotDto;
import com.jiangwensi.mrbs.dto.BookingDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.entity.BookingEntity;
import com.jiangwensi.mrbs.entity.RoomEntity;
import com.jiangwensi.mrbs.entity.Slot;
import com.jiangwensi.mrbs.entity.UserEntity;
import com.jiangwensi.mrbs.exception.InvalidInputException;
import com.jiangwensi.mrbs.exception.NotFoundException;
import com.jiangwensi.mrbs.repo.BookingRepository;
import com.jiangwensi.mrbs.repo.RoomRepository;
import com.jiangwensi.mrbs.repo.UserRepository;
import com.jiangwensi.mrbs.utils.MyDateUtils;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepo;
    private UserService userService;
    private UserRepository userRepository;
    private RoomRepository roomRepository;

    public BookingServiceImpl(BookingRepository bookingRepo, UserService userService, UserRepository userRepository, RoomRepository roomRepository) {
        this.bookingRepo = bookingRepo;
        this.userService = userService;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public BookingDto createBooking(String bookedBy, String roomId, String from, String to) throws ParseException {
        String timeformat = getTimeFormat();

        UserEntity userEntity = userRepository.findByPublicId(bookedBy);
        if (userEntity == null) {
            throw new NotFoundException("Unable to find user by id:" + bookedBy);
        }

        RoomEntity roomEntity = roomRepository.findByPublicId(roomId);
        if (roomEntity == null) {
            throw new NotFoundException("Unable to find room by id:" + roomId);
        }

        if (clash(roomId, from, to)) {
            throw new InvalidInputException("The time slot is not available. roomId:" + roomId + ",from:" + from + ",to:" + to);
        }

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setBookedBy(userEntity);
        bookingEntity.setRoom(roomEntity);
        bookingEntity.setDate(new SimpleDateFormat(timeformat).parse(from));
        bookingEntity.setFromTime(new SimpleDateFormat(timeformat).parse(from));
        bookingEntity.setToTime(new SimpleDateFormat(timeformat).parse(to));
        bookingEntity.setPublicId(UUID.randomUUID().toString());
        bookingEntity = bookingRepo.save(bookingEntity);

        BookingDto returnValue = new BookingDto();
        MyModelMapper.bookingEntityToDtoModelMapper().map(bookingEntity, returnValue);
        return returnValue;
    }


    @Override
    public BookingDto updateBooking(String publicId, String room, String fromTime, String toTime) throws ParseException {
        BookingEntity bookingEntity = bookingRepo.findByPublicId(publicId);
        if (bookingEntity == null) {
            throw new NotFoundException("Unable to find booking id:" + publicId);
        }
        if (!MyStringUtils.isEmpty(room)) {
            RoomEntity roomEntity = roomRepository.findByPublicId(room);
            bookingEntity.setRoom(roomEntity);
        }
        if (!MyStringUtils.isEmpty(fromTime)) {
            bookingEntity.setFromTime(new SimpleDateFormat(getTimeFormat()).parse(fromTime));
        }
        if (!MyStringUtils.isEmpty(toTime)) {
            bookingEntity.setToTime(new SimpleDateFormat(getTimeFormat()).parse(toTime));
        }
        bookingEntity = bookingRepo.save(bookingEntity);
        BookingDto returnValue = new BookingDto();
        MyModelMapper.bookingEntityToDtoModelMapper().map(bookingEntity, returnValue);
        return returnValue;
    }

    @Override
    public BookingDto viewBooking(String bookingId) {
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


        if(date!=null && date!=""){
            if(MyDateUtils.isValidFormat(date)) {
                throw new InvalidInputException("Invalid date format " + date);
            }
        }

        List<BookingEntity> bookingEntities = new ArrayList<>();

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
        if (result != null && result.size() > 0) {
            return true;
        }
        return false;
    }

    private String getTimeFormat() {
        return AppProperties.getAppProperties().getProperty("dateformat") + " " + AppProperties.getAppProperties().getProperty("timeformat");
    }
}
