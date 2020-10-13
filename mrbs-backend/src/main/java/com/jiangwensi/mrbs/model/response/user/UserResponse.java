package com.jiangwensi.mrbs.model.response.user;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.List;

/**
 * Created by Jiang Wensi on 22/8/2020
 */
public class UserResponse extends GeneralResponse {

    private String publicId;
    private String name;
    private String email;
    private boolean active; 
    private boolean emailVerified;
    private List<String> isAdminOfRooms;
    private List<String> isAdminOfOrganizations;
    private List<String> roles;

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

    public List<String> getIsAdminOfRooms() {
        return isAdminOfRooms;
    }

    public void setIsAdminOfRooms(List<String> isAdminOfRooms) {
        this.isAdminOfRooms = isAdminOfRooms;
    }

    public List<String> getIsAdminOfOrganizations() {
        return isAdminOfOrganizations;
    }

    public void setIsAdminOfOrganizations(List<String> isAdminOfOrganizations) {
        this.isAdminOfOrganizations = isAdminOfOrganizations;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
