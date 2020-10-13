package com.jiangwensi.mrbs.security;

import com.jiangwensi.mrbs.AppContext;
import com.jiangwensi.mrbs.repo.UserRepository;
import com.jiangwensi.mrbs.service.UserService;
import com.jiangwensi.mrbs.utils.TokenUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Jiang Wensi on 17/8/2020
 */

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization==null || !authorization.startsWith("Bearer ")){
            chain.doFilter(request,response);
            return;
        }

        String bearerToken = authorization.replace("Bearer "," ");
        String email = TokenUtils.retrieveSubject(bearerToken);
//        UserEntity userEntity =
//                ((UserRepository) AppContext.getContext().getBean("userRepository")).findByEmail(email);
//
//        UserPrincipal principal = new UserPrincipal();
//        principal.setUsername(email);
//        principal.setEnabled(userEntity.isEmailVerified());

        UserService userService = (UserService) AppContext.getContext().getBean("userServiceImpl");
        UserPrincipal principal = (UserPrincipal) userService.loadUserByUsername(email);


        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(principal,null,principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        chain.doFilter(request,response);
    }

}
