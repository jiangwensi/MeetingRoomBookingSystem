package com.jiangwensi.mrbs.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
public class UserDto {
    private String publicId;
    private String name;
    private String email;
    private String password;
    private boolean emailVerified;
    private boolean active;
    private AvatarDto avatar;
    private List<String> tokens = new ArrayList<>();
    private List<String> roles = new ArrayList<>();
    private List<BookingDto> bookings = new ArrayList<>();
    private List<String> isAdminOfOrganizations = new ArrayList<>();
    private List<String> isAdminOfRooms = new ArrayList<>();

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public AvatarDto getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarDto avatar) {
        this.avatar = avatar;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<BookingDto> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDto> bookings) {
        this.bookings = bookings;
    }

    public List<String> getIsAdminOfOrganizations() {
        return isAdminOfOrganizations;
    }

    public void setIsAdminOfOrganizations(List<String> isAdminOfOrganizations) {
        this.isAdminOfOrganizations = isAdminOfOrganizations;
    }

    public List<String> getIsAdminOfRooms() {
        return isAdminOfRooms;
    }

    public void setIsAdminOfRooms(List<String> isAdminOfRooms) {
        this.isAdminOfRooms = isAdminOfRooms;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
