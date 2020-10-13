package com.jiangwensi.mrbs.repo;

import com.jiangwensi.mrbs.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created by Jiang Wensi on 15/8/2020
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByEmail(String email);
//

    UserEntity findByPublicId(String publicId);

    @Query(nativeQuery = true,
    value="select u.* from user u " +
            "join room_user ru on u.id=ru.user_id " +
            "join room r on ru.room_id = r.id and r.public_id = :roomId")
    List<UserEntity> findByRoom(@Param("roomId") String roomId);


    @Query(nativeQuery = true,value =
            "select u.* from user u " +
                    "where u.active in (:active) " +
                    "and u.email_verified in (:verified) " +
                    "and u.name like %:name% " +
                    "and u.id in " +
                    "(select ur.user_id from user_role ur " +
                    "where ur.role_id in " +
                    "(select r.id from role r " +
                    "where r.name in (:role)))")
    List<UserEntity> searchByName(@Param("name") String name,
                                  @Param("role") List<String> role,
                                  @Param("active") List<Boolean> active,
                                  @Param("verified") List<Boolean> verified);

    @Query(nativeQuery = true,value =
            "select u.* from user u " +
                    "where u.active in (:active) " +
                    "and u.email_verified in (:verified) " +
                    "and upper(u.email) like %:email% " +
                    "and u.id in " +
                    "(select ur.user_id from user_role ur " +
                    "where ur.role_id in " +
                    "(select r.id from role r " +
                    "where r.name in (:role)))")
    List<UserEntity> searchByEmail( @Param("email") String email,
                                    @Param("role") List<String> role,
                                    @Param("active") List<Boolean> active,
                                    @Param("verified") List<Boolean> verified);

    @Query(nativeQuery = true,value =
            "select u.* from user u " +
                    "where u.active in (:active) " +
                    "and u.email_verified in (:verified) " +
                    "and u.email like %:email% " +
                    "and u.name like %:name% " +
                    "and u.id in " +
                    "(select ur.user_id from user_role ur " +
                    "where ur.role_id in " +
                    "(select r.id from role r " +
                    "where r.name in (:role)))")
    List<UserEntity> searchByAllFields(@Param("email") String email,
                                       @Param("name") String name,
                                       @Param("role") List<String> role,
                                       @Param("active") List<Boolean> active,
                                       @Param("verified") List<Boolean> verified);

    @Query(nativeQuery = true,value =
            "select u.* from user u " +
                    "where u.active in (:active) " +
                    "and u.email_verified in (:verified) " +
                    "and u.id in " +
                    "(select ur.user_id from user_role ur " +
                    "where ur.role_id in " +
                    "(select r.id from role r " +
                    "where r.name in (:role)))")
    List<UserEntity> search(
            @Param("role") List<String> role,
            @Param("active") List<Boolean> active,
            @Param("verified") List<Boolean> verified);



//    @Query(nativeQuery = true,
//            value= "select u.* from user u " +
//                    "join user_role ur on u.id = ur.user_id "+
//                    "join role r on ur.role_id = r.id " +
//                    "where r.name in (:role) " +
//                    "and u.active in (:active)" +
//                    "and u.email_verified in (:verified)")
//    List<UserEntity> search(
//            @Param("role") List<String> role,
//            @Param("active") List<Boolean> active,
//            @Param("verified") List<Boolean> verified);



}
