package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.dto.TokenDto;
import com.jiangwensi.mrbs.entity.TokenEntity;
import com.jiangwensi.mrbs.entity.UserEntity;
import com.jiangwensi.mrbs.enumeration.TokenType;
import com.jiangwensi.mrbs.repo.TokenRepository;
import com.jiangwensi.mrbs.repo.UserRepository;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import com.jiangwensi.mrbs.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Jiang Wensi on 20/8/2020
 */

@Service
@Slf4j
public class PasswordServiceImpl implements PasswordService{
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public TokenDto generateResetPasswordToken(String email,String returnUrl) {
        TokenDto returnValue = new TokenDto();
        if(StringUtils.isEmpty(email)){
            return null;
        }
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity==null){
            return null;
        }

        TokenEntity obsoleteToken = null;
        List<TokenEntity> tokens = userEntity.getTokens();
        for(TokenEntity tokenEntity:tokens){
            if(tokenEntity.getType().equals(TokenType.RESET_FORGOTTEN_PASSWORD)){
                obsoleteToken = tokenEntity;
                break;
            }
        }
        if (obsoleteToken != null) {
            tokenRepository.deleteById(obsoleteToken.getId());
            userEntity.getTokens().remove(obsoleteToken);
        }

        Long tokenValidity = Long.valueOf(AppProperties.getAppProperties().getProperty("password.reset.token.validity"));
        String token = TokenUtils.generateToken(email,tokenValidity);
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setType(TokenType.RESET_FORGOTTEN_PASSWORD.toString());
        tokenEntity.setToken(token);
        tokenEntity.setReturnUrl(returnUrl);
        userEntity.getTokens().add(tokenEntity);

        userRepository.save(userEntity);
        MyModelMapper.userEntityToUserDtoModelMapper().map(tokenEntity,returnValue);
        return returnValue;
    }

    @Override
    public boolean checkPasswordMatch(String email, String oldPassword) {
        UserEntity userEntity = userRepository.findByEmail(email);
        boolean matches = bCryptPasswordEncoder.matches(oldPassword,userEntity.getPassword());
        return matches;
    }

    @Override
    public void removeResetPasswordToken(String email) {

    }
}
