package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.dto.TokenDto;
import com.jiangwensi.mrbs.dto.VerifyEmailTokenDto;
import com.jiangwensi.mrbs.entity.TokenEntity;
import com.jiangwensi.mrbs.entity.UserEntity;
import com.jiangwensi.mrbs.enumeration.TokenType;
import com.jiangwensi.mrbs.exception.InvalidInputException;
import com.jiangwensi.mrbs.exception.UnknownErrorException;
import com.jiangwensi.mrbs.repo.TokenRepository;
import com.jiangwensi.mrbs.repo.UserRepository;
import com.jiangwensi.mrbs.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Jiang Wensi on 17/8/2020
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Override
    public VerifyEmailTokenDto verifyToken(String token) {
        VerifyEmailTokenDto returnValue = new VerifyEmailTokenDto();
        TokenEntity tokenEntity = tokenRepository.findByToken(token);

        if (tokenEntity == null) {
            logger.error("unable to find token " + token);
            throw new InvalidInputException("Token is invalid or this email has already been verified.");
        }

        UserEntity userEntity = tokenEntity.getUser();
        if (userEntity == null) {
            logger.error("unable to find userEntity from token " + token);
            throw new UnknownErrorException("Token is invalid");
        }

        String email = userEntity.getEmail();
/**********************    redundant verification **********************/
//        String subject = claims.getSubject();
//        if (!subject.equals(email)){
//            returnValue.setValid(false);
//            returnValue.setMessage("Invalid Token");
//            return returnValue;
//        }

        tokenRepository.delete(tokenEntity);

        returnValue.setValid(true);
        returnValue.setMessage("Email verification is successful");
        returnValue.setEmail(email);
        return returnValue;
    }

    @Override
    public boolean tokenExists(String token) {
        return tokenRepository.findByToken(token) != null;
    }

    @Override
    public void removeToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    //TODO
    @Override
    public TokenDto findTokenByUserEmailAndType(String myEmail, String tokenType) {
//        TokenEntity tokenEntity = tokenRepository.findByEmailAndType();
        return null;
    }


    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void deleteObsoleteTokenVerifyEmail(String userPublicId) {
        UserEntity userEntity = userRepository.findByPublicId(userPublicId);
        TokenEntity existingToken = tokenRepository.findByUserIdAndType(userEntity.getId(),
                TokenType.VERIFY_EMAIL.name());
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
            userEntity.getTokens().remove(existingToken);
            userRepository.save(userEntity);
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void generateVerifyUpdatedEmailToken(String email, String changeEmailReturnUrl, String userPublicId) {
        UserEntity userEntity = userRepository.findByPublicId(userPublicId);
        //add new verify email token
        Long tokenValidity = Long.valueOf(AppProperties.getAppProperties().getProperty("email.verify.token.validity"));
        String emailVerificationToken = TokenUtils.generateToken(email, tokenValidity);
        userEntity.setEmailVerified(false);
        userEntity.setEmail(email);

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(emailVerificationToken);
        tokenEntity.setUser(userEntity);
        tokenEntity.setType(TokenType.VERIFY_EMAIL.name());
        tokenEntity.setReturnUrl(changeEmailReturnUrl);
//            tokenEntities.add(tokenEntity);
        tokenRepository.save(tokenEntity);
        userEntity.getTokens().add(tokenEntity);
        userRepository.save(userEntity);
//            tokenRepository.saveAll(tokenEntities);
//            userEntity.setTokens(tokenEntities);
    }

}
