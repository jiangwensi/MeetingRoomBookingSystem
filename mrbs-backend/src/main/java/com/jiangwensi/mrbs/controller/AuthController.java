package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.dto.TokenDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.dto.VerifyEmailTokenDto;
import com.jiangwensi.mrbs.exception.DuplicateException;
import com.jiangwensi.mrbs.model.request.auth.RequestResetForgottenPasswordRequest;
import com.jiangwensi.mrbs.model.request.auth.ResetForgottenPasswordRequest;
import com.jiangwensi.mrbs.model.request.auth.ResetPasswordRequest;
import com.jiangwensi.mrbs.model.request.auth.SignUpRequest;
import com.jiangwensi.mrbs.model.response.GeneralResponse;
import com.jiangwensi.mrbs.model.response.auth.SignUpResponse;
import com.jiangwensi.mrbs.model.response.auth.VerifyEmailResponse;
import com.jiangwensi.mrbs.service.UserService;
import com.jiangwensi.mrbs.service.PasswordService;
import com.jiangwensi.mrbs.service.SESService;
import com.jiangwensi.mrbs.service.TokenService;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import com.jiangwensi.mrbs.utils.TokenUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@RestController
@RequestMapping(PathConst.AUTH_PATH)
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Scope("request")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private SESService SESService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordService passwordService;

    @PostMapping(path="signUp",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        String name = request.getName();
        String email = MyStringUtils.toUpperCaseAndTrim(request.getEmail());
        String password = request.getPassword();
        String returnUrl = request.getReturnUrl();

        ModelMapper mapper = new ModelMapper();
        PropertyMap<UserDto, SignUpResponse> map = new PropertyMap<UserDto, SignUpResponse>() {
            @Override
            protected void configure() {
                map().setUserId(source.getPublicId());
            }
        };
        mapper.addMappings(map);

        UserDto userDto = userService.findUserByEmail(email);
        if (userDto != null) {
            if (userDto.isEmailVerified()) {
                throw new DuplicateException("User with email [" + email + "] already exists");
            } else {
                userDto = userService.regenerateEmailVerifyToken(name,email,password,returnUrl);
            }
        } else {
            userDto = userService.createUser(name, email, password, returnUrl);
        }

        SESService.sendEmailVerification(userDto);

        SignUpResponse returnValue = mapper.map(userDto, SignUpResponse.class);
        returnValue.setMessage("Verification link is sent to " + email + ", please login your email and click the " +
                "verification link to complete the sign up process. Thanks!");

        return returnValue;
    }

    @GetMapping(path = "/verifyEmail")
    public VerifyEmailResponse verify(@RequestParam("token") String token) {
        VerifyEmailTokenDto verifyTokenDto = tokenService.verifyToken(token);
        userService.emailVerified(verifyTokenDto.getEmail());
        VerifyEmailResponse returnValue = new ModelMapper().map(verifyTokenDto, VerifyEmailResponse.class);
        return returnValue;
    }

    @PostMapping(path = "/requestResetForgottenPassword")
    public GeneralResponse requestResetForgottenPassword(@RequestBody RequestResetForgottenPasswordRequest request) {
        String email = MyStringUtils.toUpperCaseAndTrim(request.getEmail());

        GeneralResponse returnValue = new GeneralResponse();
        UserDto userDto = userService.findUserByEmail(email);
        TokenDto tokenDto = passwordService.generateResetPasswordToken(email, request.getReturnUrl());
        SESService.sendResetForgottenPasswordTokenEmail(userDto, tokenDto);
        //email token
        returnValue.setMessage("You will receive email to reset your password if the email address is valid is valid" +
                ".\n Please check " +
                "your email at " + email);
        return returnValue;
    }

    @PostMapping(path = "/resetForgottenPassword")
    public GeneralResponse resetForgottenPassword(@RequestBody ResetForgottenPasswordRequest request) {
        GeneralResponse returnValue = new GeneralResponse();

        String email = request.getEmail();
        String password = request.getPassword();
        String token = request.getToken();

        String subject ;
        try {
            subject = TokenUtils.retrieveSubject(token);
        } catch(Exception e){
            logger.error(e.getMessage(),e);
            returnValue.setErrorMessage("Invalid token");
            returnValue.setStatus("fail");
            return returnValue;
        }

        if(!email.toUpperCase().equals(subject.toUpperCase())){
            returnValue.setErrorMessage("Invalid token");
            returnValue.setStatus("fail");
            return returnValue;
        }

        if(!tokenService.tokenExists(token)){
            returnValue.setErrorMessage("Invalid token");
            returnValue.setStatus("fail");
            return returnValue;
        } else {
            tokenService.removeToken(token);
        }

        returnValue.setMessage("Password is reset successfully");
        returnValue.setStatus("success");
        return returnValue;
    }

    @PostMapping(path = "/resetPassword")
    public GeneralResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        GeneralResponse returnValue = new GeneralResponse();

        String email = MyStringUtils.toUpperCaseAndTrim(request.getEmail());
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        boolean oldPasswordMatch = passwordService.checkPasswordMatch(email,oldPassword);

        if(!oldPasswordMatch){
            returnValue.setErrorMessage("Old password is wrong. Please try again");
            returnValue.setStatus("fail");
            return returnValue;
        }

        userService.updatePassword(email,newPassword);
        returnValue.setMessage("Password is reset successfully");
        returnValue.setStatus("success");
        return returnValue;
    }

}
