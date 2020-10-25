package com.jiangwensi.mrbs.repo;

import com.jiangwensi.mrbs.entity.BookingEntity;
import com.jiangwensi.mrbs.entity.Slot;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public interface BookingRepository extends CrudRepository<BookingEntity, Long> {

    @Query(nativeQuery = true,
            value = "select b.* from booking b " +
                    "join room r on b.room_id = r.id " +
                    "where r.public_id=:roomId " +
                    "and ((:fromTime < b.from_time and :toTime>b.from_time) " +
                    "or (:toTime>b.to_time and :fromTime < b.to_time) " +
                    "or(:toTime<b.to_time and :fromTime > b.from_time))")
    List<BookingEntity> checkClash(@Param("roomId") String roomId, @Param("fromTime") String from, @Param("toTime") String to);

    BookingEntity findByPublicId(String bookingId);

    @Query(nativeQuery = true,
            value = "select b.* from booking b " +
                    "join room r on b.room_id = r.id " +
                    "join user u on u.id = b.booked_by " +
                    "where r.public_id like concat('%',ifnull(:roomPublicId,''),'%') " +
                    "and (b.date = :date or :date is null) " +
                    "and u.public_id = :bookedBy  " +
                    "order by b.from_time asc")
    List<BookingEntity> search(@Param("bookedBy") String bookedBy,
                               @Param("roomPublicId") String roomPublicId,
                               @Param("date") String date);

    @Query(nativeQuery = true,
            value = "select b.* from booking b " +
                    "join room r on b.room_id = r.id " +
                    "where r.public_id like concat('%',ifnull(:roomPublicId,''),'%') " +
                    "and (b.date = :date  or :date is null) order by b.from_time asc")
    List<BookingEntity> searchBySysAdm(@Param("roomPublicId") String roomPublicId, @Param("date") String date);

    @Query(nativeQuery = true, value = "call getAvailableTimeslot(:roomId,:date)")
    List<Slot> getAvailableSlots(@Param("roomId") String roomId, @Param("date") String date);

    @Modifying
    @Query(nativeQuery = true, value = "delete from booking where room_id in (select id from room where " +
            "public_id =:roomId) ")
    void deleteBookingByRoomPublicId(@Param("roomId") String roomId);

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Modifying
    @Query(nativeQuery = true, value = "delete from booking where room_id in (select id from room where " +
            "id =:roomId) ")
    void deleteByRoomId(@Param("roomId") Long roomId);


}
