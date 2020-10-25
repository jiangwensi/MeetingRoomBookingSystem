package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.exception.InvalidInputException;
import com.jiangwensi.mrbs.exception.UnknownErrorException;
import com.jiangwensi.mrbs.model.request.user.UpdateMyProfileRequest;
import com.jiangwensi.mrbs.model.request.user.UpdateUserRequest;
import com.jiangwensi.mrbs.model.response.GeneralResponse;
import com.jiangwensi.mrbs.model.response.user.SearchUserResponse;
import com.jiangwensi.mrbs.model.response.user.UserResponse;
import com.jiangwensi.mrbs.service.TokenService;
import com.jiangwensi.mrbs.service.UserService;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jiang Wensi on 22/8/2020
 */
@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Scope("request")
public class UserController {

    private UserService userService;
    private TokenService tokenService;
    private com.jiangwensi.mrbs.service.SESService SESService;

    public UserController(UserService userService, TokenService tokenService, com.jiangwensi.mrbs.service.SESService SESService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.SESService = SESService;
    }

    @GetMapping(PathConst.USER_PATH)
    @PreAuthorize("hasAuthority('SYSADM')")
    public SearchUserResponse searchUser(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, name = "role") List<String> roles,
            @RequestParam(required = false, name = "active") List<Boolean> actives,
            @RequestParam(required = false, name = "verified") List<Boolean> verifies,
            @RequestParam(required = false, name = "verbose") Boolean verbose) {

        email = MyStringUtils.toUpperCaseAndTrim(email);
        roles = MyStringUtils.toUpperCaseAndTrim(roles);

        List<UserDto> userDtos = userService.search(name, email, roles, actives, verifies,verbose);

        SearchUserResponse returnValue = new SearchUserResponse();
        if (userDtos
                != null && userDtos.size() > 0) {

            try {
                returnValue.setUsers(new ModelMapper().map(userDtos, new TypeToken<List<UserResponse>>() {
                }.getType()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new UnknownErrorException("Something went wrong");
            }
        }

        returnValue.setMessage("Results found");
        returnValue.setStatus("success");
        return returnValue;
    }

    @GetMapping(PathConst.USER_PATH + "/{publicId}")
    @PreAuthorize("hasAuthority('SYSADM')")
    public UserResponse viewUser(@PathVariable String publicId) {
        UserDto userDto = userService.findUserByPublicId(publicId);
        UserResponse returnValue = new UserResponse();

        new ModelMapper().map(userDto, returnValue);
        returnValue.setStatus(MyResponseStatus.success.toString());

        return returnValue;
    }

    @GetMapping("myprofile/view")
    public UserResponse viewMyProfile() {

        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (myEmail == null && myEmail.length() == 0) {
            throw new UnknownErrorException("Something went wrong");
        }

        UserDto userDto = userService.findUserByEmail(myEmail);
        UserResponse returnValue = new UserResponse();
        new ModelMapper().map(userDto, returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;

    }

    @PatchMapping("/myprofile/update")
    public UserResponse updateMyProfile(@RequestBody UpdateMyProfileRequest request) {
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (myEmail == null && myEmail.length() == 0) {
            throw new UnknownErrorException("Something went wrong");
        }

        UserDto myDto = userService.findUserByEmail(myEmail);

        if(myDto==null||!myDto.getPublicId().equals(request.getPublicId())){
            throw new UnknownErrorException("Something went wrong");
        }

        UserDto userDto = userService.updateMyProfile(request.getPublicId(),request.getName(),request.getEmail(),
                request.getChangeEmailReturnUrl());

        Boolean isEmailUpdated = false;
        if(!StringUtils.isEmpty(request.getEmail())&&!request.getEmail().equals(myEmail)){
            isEmailUpdated = true;
            SESService.sendEmailVerification(userDto);
        }

        UserResponse returnValue = new UserResponse();

        new ModelMapper().map(userDto, returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage(isEmailUpdated?"Verification email has been sent to "+userDto.getEmail()+",please " +
                "check your mailbox and verify":"");

        return returnValue;

    }

    @PatchMapping(PathConst.USER_PATH)
    @PreAuthorize("hasAuthority('SYSADM')")
    public UserResponse updateUser(@RequestBody UpdateUserRequest request) {

        userService.updateUser( request);
        UserDto updatedUser = userService.findUserByPublicId(request.getPublicId());

        if (updatedUser == null) {
            throw new UnknownErrorException("Something went wrong.");
        }

        UserResponse returnValue = new UserResponse();

        MyModelMapper.userDtoToUserResponseModelMapper(returnValue).map(updatedUser, returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }

    @DeleteMapping(PathConst.USER_PATH + "/{publicId}")
    @PreAuthorize("hasAuthority('SYSADM')")
    public GeneralResponse deleteUser(@PathVariable String publicId) {
        try{
            userService.deleteUser(publicId);
        } catch (DataIntegrityViolationException e){
            throw new InvalidInputException("Unable to delete this user. Please make sure this user is not admin or " +
                    "user of a room or organization");
        }
        GeneralResponse returnValue = new GeneralResponse();
        returnValue.setStatus(MyResponseStatus.success.toString());
        returnValue.setMessage("User is deleted");
        return returnValue;
    }
}
