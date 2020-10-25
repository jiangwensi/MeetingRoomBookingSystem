package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.VerifyEmailTokenDto;

import javax.transaction.Transactional;

/**
 * Created by Jiang Wensi on 17/8/2020
 */

public interface TokenService {
    VerifyEmailTokenDto verifyToken(String token);

    boolean tokenExists(String token);

    void removeToken(String token);

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void deleteObsoleteTokenVerifyEmail(String userPublicId);

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void generateVerifyUpdatedEmailToken(String email, String changeEmailReturnUrl, String userPublicId);
}
