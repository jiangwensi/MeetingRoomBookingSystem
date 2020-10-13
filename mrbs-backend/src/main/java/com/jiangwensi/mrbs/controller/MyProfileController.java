package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.PathConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Jiang Wensi on 24/8/2020
 */
@RestController
@RequestMapping(PathConst.MYPROFILE_PATH)
@Slf4j
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class MyProfileController {

//    @Autowired
//    private UserService userService;
//
//    @PatchMapping
//    public UserResponse updateProfile(@RequestBody UpdateProfileRequest request) {
//
//        String publicId = request.getPublicId();
//        String name = request.getName();
//
//        log.info("updateProfile publicId:"+publicId+", name="+name);
//
//        isAccessingMyAccount(publicId);
//
//        UserResponse returnValue = new UserResponse();
//
//        if (publicId == null || publicId.trim().equals("")) {
//            throw new InvalidInputException("Invalid id:"+publicId);
//        }
//
//        userService.editProfile(publicId,name);
//
//        UserDto userDto = userService.findUserByPublicId(publicId);
//
//        MyModelMapper.userDtoToUserResponseModelMapper(returnValue).map(userDto,returnValue);
//        returnValue.setMessage("Profile is updated successfully");
//        returnValue.setStatus(MyResponseStatus.success.toString());
//
//        return returnValue;
//    }

//    @GetMapping("/{publicId}")
//    public UserResponse viewUser(@PathVariable String publicId) {
//        log.info("viewUser publicId:" + publicId);
//
//        isAccessingMyAccount(publicId);
//
//        UserDto userDto = userService.findUserByPublicId(publicId);
//        UserResponse returnValue = new UserResponse();
//
//        new ModelMapper().map(userDto, returnValue);
//        returnValue.setStatus(MyResponseStatus.success.toString());
//
//        return returnValue;
//    }
//
//    private void isAccessingMyAccount(String publicId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDto userDto = userService.findUserByPublicId(publicId);
//        if(!userDto.getEmail().equals(auth.getName())){
//            throw new AccessDeniedException("You are not authorized to perform edit this profile");
//        }
//    }
}
