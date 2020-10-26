package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.entity.TokenEntity;
import com.jiangwensi.mrbs.model.request.user.UpdateUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService extends UserDetailsService {
    void removeObsoleteToken(List<TokenEntity> tokenEntities);
    UserDto findUserByEmail(String email);

    UserDto createUser(String name, String email, String password,String returnUrl);

    UserDto emailVerified(String email);

    UserDto regenerateEmailVerifyToken(String name, String email, String password, String returnUrl);

    UserDto updatePassword(String email, String password);


    List<UserDto> search(String name, String email, List<String> role, List<Boolean> active,
                         List<Boolean> verified,Boolean verbose);


    UserDto findUserByPublicId(String publicId);

    void deleteUser(String publicId);

    void updateUser(UpdateUserRequest request);

    UserDto updateMyProfile(String publicId, String name, String email, String changeEmailReturnUrl);

    boolean isAccessingMyOrg(String orgPublicId);


    boolean isAccessedByRoomUser(String roomId);

    boolean isAccessedByTargetRole(String role);

    boolean isAccessedByRoomAdmin(String roomId);

    boolean isAccessingMyBooking(String bookingId);

    boolean isAccessingMyRoomOrgAdmin(String roomPublicId);

    boolean isOrgAdminAccessingRoom(String roomPublicId);

    boolean isRoomAdminAccessingRoom(String roomPublicId);

    boolean isSysadm();
}
