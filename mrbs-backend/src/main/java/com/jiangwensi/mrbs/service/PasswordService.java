package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.TokenDto;

/**
 * Created by Jiang Wensi on 20/8/2020
 */
public interface PasswordService {
    TokenDto generateResetPasswordToken(String email, String returnUrl);

    boolean checkPasswordMatch(String email, String oldPassword);

    void removeResetPasswordToken(String email);
}
