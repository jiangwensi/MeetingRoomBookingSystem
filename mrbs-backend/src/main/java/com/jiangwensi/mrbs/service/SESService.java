package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.TokenDto;
import com.jiangwensi.mrbs.dto.UserDto;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
public interface SESService {

    void sendEmailVerification(UserDto userDto);
    void sendResetForgottenPasswordTokenEmail(UserDto userDto, TokenDto tokenDto);
}
