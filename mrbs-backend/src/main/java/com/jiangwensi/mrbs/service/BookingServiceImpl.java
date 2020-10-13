package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.dto.BookingDto;
import com.jiangwensi.mrbs.entity.BookingEntity;
import com.jiangwensi.mrbs.entity.RoomEntity;
import com.jiangwensi.mrbs.entity.UserEntity;
import com.jiangwensi.mrbs.exception.InvalidInputException;
import com.jiangwensi.mrbs.exception.NotFoundException;
import com.jiangwensi.mrbs.repo.BookingRepository;
import com.jiangwensi.mrbs.repo.RoomRepository;
import com.jiangwensi.mrbs.repo.UserRepository;
import com.jiangwensi.mrbs.utils.MyDateUtils;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

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
    public List<BookingDto> search(Boolean isSysAdm,String bookedBy, String roomName, String fromDate, String toDate) {
        List<BookingEntity> bookingEntities = new ArrayList<>();

//        boolean hasBookedBy = !StringUtils.isEmpty(bookedBy);
//
//        boolean hasRoomId = !StringUtils.isEmpty(roomName);

        if(fromDate!=null && fromDate!=""){
            if(MyDateUtils.isValidFormat(fromDate)) {
                throw new InvalidInputException("Invalid date format " + fromDate);
            }
        }else {
            fromDate ="1000-01-01";
        }

        if(toDate!=null && toDate!=""){
            if(MyDateUtils.isValidFormat(toDate)){
                throw new InvalidInputException("Invalid date format "+toDate);
            }
        } else {
            toDate = "9999-12-31";
        }

        if(isSysAdm){
            bookingEntities = bookingRepo.searchBySysAdm(roomName, fromDate, toDate);
        } else {
            bookingEntities = bookingRepo.search(bookedBy, roomName, fromDate, toDate);
        }




//
//        if (!hasBookedBy && !hasRoomId && !hasDate) {
//
//            bookingEntities = IteratorUtils.toList(bookingRepo.findAll().iterator());
//
//        } else if (hasBookedBy && !hasRoomId && !hasDate) {
//
//            bookingEntities = bookingRepo.searchByBookedBy(bookedBy);
//
//        } else if (!hasBookedBy && hasRoomId && !hasDate) {
//
//            bookingEntities = bookingRepo.searchByRoomId(roomId);
//
//        } else if (!hasBookedBy && !hasRoomId && hasDate) {
//
//            bookingEntities = bookingRepo.searchByDate(date);
//
//        } else if (!hasBookedBy && hasRoomId && hasDate) {
//
//            bookingEntities = bookingRepo.searchByRoomIdAndDate(roomId, date);
//
//        } else if (hasBookedBy && !hasRoomId && hasDate) {
//
//            bookingEntities = bookingRepo.searchByBookedByAndDate(bookedBy, date);
//
//        } else if (hasBookedBy && hasRoomId && !hasDate) {
//
//            bookingEntities = bookingRepo.searchByBookedByAndRoomId(bookedBy, roomId);
//
//        } else if (hasBookedBy && hasRoomId && hasDate) {
//
//            bookingEntities = bookingRepo.search(bookedBy, roomId, date);
//
//        }

        List<BookingDto> returnValue = new ArrayList<>();
        for (BookingEntity bookingEntity : bookingEntities) {
            BookingDto bookingDto = new BookingDto();
            MyModelMapper.bookingEntityToDtoModelMapper().map(bookingEntity, bookingDto);
            returnValue.add(bookingDto);
        }
        return returnValue;
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
