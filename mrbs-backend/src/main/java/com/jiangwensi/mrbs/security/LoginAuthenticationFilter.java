package com.jiangwensi.mrbs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jiangwensi.mrbs.AppProperties;
import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.exception.UnknownErrorException;
import com.jiangwensi.mrbs.model.request.auth.LoginRequest;
import com.jiangwensi.mrbs.model.response.auth.LoginResponse;
import com.jiangwensi.mrbs.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jiang Wensi on 17/8/2020
 */
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(LoginAuthenticationFilter.class);
    private AuthenticationManager authenticationManager;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        logger.debug("attemptAuthentication() is called");
        LoginRequest loginRequestModel = null;
        try {
            loginRequestModel = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            throw new UnknownErrorException("Error when read username and password");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestModel.getEmail(),loginRequestModel.getPassword());

        return this.authenticationManager.authenticate(authenticationToken);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        logger.debug("successfulAuthentication is called");
        UserPrincipal userPrincipal= (UserPrincipal) authResult.getPrincipal();
        String userId = userPrincipal.getPublicId();
        String email = userPrincipal.getUsername();
        Long tokenValidity = Long.valueOf(AppProperties.getAppProperties().getProperty("login.token.validity"));
        String token = TokenUtils.generateToken(email,tokenValidity);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAuthToken("Bearer "+token);

        Set<String> roles = new HashSet<>();
        if(authResult!=null){
            authResult.getAuthorities().forEach(e-> roles.add(e.getAuthority()));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.createArrayNode();
        loginResponse.setRoles(roles);
        loginResponse.setStatus(MyResponseStatus.success.name());
        loginResponse.setMessage("Login is successful");
        loginResponse.setName(userPrincipal.getName());

        String responseJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(loginResponse);

        response.getWriter().write(responseJson);
//        response.addHeader(HttpHeaders.AUTHORIZATION,"Bearer "+token);
//        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,"Authorization,userid");
//        response.addHeader("userid", userId);
    }

}
