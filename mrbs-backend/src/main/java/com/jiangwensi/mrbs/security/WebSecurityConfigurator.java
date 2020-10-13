package com.jiangwensi.mrbs.security;

import com.amazonaws.http.HttpMethodName;
import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurator extends WebSecurityConfigurerAdapter {

    private UserService userService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfigurator(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .cors().and()
                .cors().configurationSource(getCorsConfigurationSource()).and()
//                .cors().configurationSource(request->configCors()).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, PathConst.AUTH_PATH+"/signUp").permitAll()
                .antMatchers(HttpMethod.GET, PathConst.AUTH_PATH+"/verifyEmail").permitAll()
                .antMatchers(HttpMethod.POST, PathConst.AUTH_PATH+"/requestResetForgottenPassword").permitAll()
                .antMatchers(HttpMethod.POST, PathConst.AUTH_PATH+"/resetForgottenPassword").permitAll()
                .antMatchers(HttpMethod.POST, PathConst.AUTH_PATH+"/resetPassword").permitAll()
//                .antMatchers(HttpMethod.GET, "/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/**").permitAll()
//                .antMatchers(HttpMethod.PATCH, "/**").permitAll()
//                .antMatchers(HttpMethod.DELETE, "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new LoginAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
//                .addFilter(new BearerTokenAuthenticationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    public CorsConfigurationSource getCorsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.applyPermitDefaultValues();
        cors.addAllowedMethod(HttpMethodName.PATCH.name());
        cors.addAllowedMethod(HttpMethodName.DELETE.name());

        return request -> cors;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    private CorsConfiguration configCors(){
        CorsConfiguration corsConfig = new CorsConfiguration();

        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("POST");
        allowedMethods.add("GET");
        allowedMethods.add("PATCH");
        allowedMethods.add("DELETE");

        List<String> alloweOrigin = new ArrayList<>();
        alloweOrigin.add("http://localhost:3000");
        corsConfig.setAllowedOrigins(alloweOrigin);
        corsConfig.setAllowedMethods(allowedMethods);
        return corsConfig;
    }

}
