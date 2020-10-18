package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.RoleDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.entity.TokenEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserService extends UserDetailsService {
    void removeObsoleteToken(List<TokenEntity> tokenEntities);
    UserDto findUserByEmail(String email);

    UserDto createUser(String name, String email, String password,String returnUrl);

    UserDto emailVerified(String email);

    UserDto regenerateEmailVerifyToken(String name, String email, String password, String returnUrl);

    UserDto updatePassword(String email, String password);

//    List<UserDto> search(String name, String email, List<String> role);

    void changeUserStatus(String publicId, boolean active);

    List<RoleDto> changeUserRoles(String publicId, List<String> changeRoles);

    List<UserDto> search(String name, String email, List<String> role, List<Boolean> active,
                         List<Boolean> verified);

    UserDto findUserByPublicId(String publicId);

    void deleteUser(String publicId);

    UserDto editProfile(String publicId, String name);

    void updateUser(String publicId, String name, List<String> changeRoles, Boolean active);

    UserDto updateMyProfile(String publicId, String name, String email, String changeEmailReturnUrl);

}
