package com.jiangwensi.mrbs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.dto.VerifyEmailTokenDto;
import com.jiangwensi.mrbs.exception.DuplicateException;
import com.jiangwensi.mrbs.model.request.auth.SignUpRequest;
import com.jiangwensi.mrbs.model.response.auth.SignUpResponse;
import com.jiangwensi.mrbs.model.response.auth.VerifyEmailResponse;
import com.jiangwensi.mrbs.service.PasswordService;
import com.jiangwensi.mrbs.service.SESService;
import com.jiangwensi.mrbs.service.TokenService;
import com.jiangwensi.mrbs.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @MockBean
    UserService userService;

    @MockBean
    SESService SESService;

    @MockBean
    TokenService tokenService;

    @MockBean
    PasswordService passwordService;

    @Autowired
    MockMvc mockMvc;

    UserDto userDto;

    SignUpRequest request;

    @BeforeEach
    void setUp() {

        request = new SignUpRequest();
        request.setEmail("TEST@EMAIL.COM");
        request.setName("testname");
        request.setPassword("testpassword");
        request.setReturnUrl("testreturnurl");

        userDto = new UserDto();
        userDto.setName("testname");
        userDto.setEmail("TEST@EMAIL.COM");
        userDto.setPublicId("publicId");
    }

    @AfterEach
    void tearDown() {
        request = null;
        userDto = null;
    }

    @Test
    void signUpNewUser() throws Exception {

        given(userService.findUserByEmail(request.getEmail())).willReturn(null);
        given(userService.createUser(request.getName(), request.getEmail(), request.getPassword(),
                request.getReturnUrl())).willReturn(userDto);

        MvcResult result = mockMvc.perform(
                post("/auth/signUp")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        SignUpResponse response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), SignUpResponse.class);

        assertEquals(userDto.getPublicId(), response.getUserId());
        assertEquals(userDto.getName(), response.getName());
        assertEquals(userDto.getEmail(), response.getEmail());
        assertEquals("Verification email is sent. Please login the mailbox to verify your email", response.getMessage());

        verify(userService, times(1)).findUserByEmail(anyString());
        verify(userService, times(1)).createUser(anyString(), anyString(), anyString(), anyString());
        verify(SESService, times(1)).sendEmailVerification(any(UserDto.class));

    }

    @Test
    void signUpReVerifyUser() throws Exception {
        userDto.setEmailVerified(false);

        given(userService.findUserByEmail(request.getEmail())).willReturn(userDto);
        given(userService.regenerateEmailVerifyToken(request.getName(), request.getEmail(), request.getPassword(),
                request.getReturnUrl())).willReturn(userDto);

        MvcResult result = mockMvc.perform(
                post("/auth/signUp")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        SignUpResponse response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), SignUpResponse.class);
        assertEquals(userDto.getPublicId(), response.getUserId());
        assertEquals(userDto.getName(), response.getName());
        assertEquals(userDto.getEmail(), response.getEmail());
        assertEquals("Verification email is sent. Please login the mailbox to verify your email", response.getMessage());
        verify(userService, times(1)).findUserByEmail(anyString());
        verify(userService, times(1)).regenerateEmailVerifyToken(anyString(), anyString(), anyString(), anyString());
        verify(userService, times(0)).createUser(anyString(), anyString(), anyString(), anyString());
        verify(SESService, times(1)).sendEmailVerification(any(UserDto.class));
    }

    @Test
    void signUpDuplicateUser() throws Exception {

        userDto.setEmailVerified(true);

        given(userService.findUserByEmail(request.getEmail())).willReturn(userDto);


        MvcResult mvnResult = mockMvc.perform(
                post("/auth/signUp")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateException))
                .andExpect(result -> assertEquals("User with email [TEST@EMAIL.COM] already exists",
                        result.getResolvedException().getMessage()))
                .andReturn();

        verify(userService, times(1)).findUserByEmail(anyString());
        verify(userService, times(0)).createUser(anyString(), anyString(), anyString(), anyString());
        verify(SESService, times(0)).sendEmailVerification(any(UserDto.class));
    }


    @Test
    void verifyEmail() throws Exception {
        VerifyEmailTokenDto verifyEmailTokenDto = new VerifyEmailTokenDto();
        verifyEmailTokenDto.setEmail("TEST@EMAIL.COM");
        verifyEmailTokenDto.setMessage("Test Message");
        verifyEmailTokenDto.setValid(true);
        verifyEmailTokenDto.setReturnUrl("Test Return Url");
        given(tokenService.verifyToken("testtoken")).willReturn(verifyEmailTokenDto);

        MvcResult result = mockMvc.perform(
                get("/auth/verifyEmail?token=testtoken")).andExpect(status().isOk()).andReturn();

        VerifyEmailResponse response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), VerifyEmailResponse.class);
        assertEquals(response.isValid(), true);

        verify(userService, times(1)).emailVerified(anyString());
        verify(tokenService, times(1)).verifyToken(anyString());

    }
}