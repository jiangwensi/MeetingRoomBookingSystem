package com.jiangwensi.mrbs.model.request.user;

import java.util.List;

/**
 * Created by Jiang Wensi on 22/8/2020
 */
public class UpdateUserRequest {
    private String action;
    private String publicId;
    private String name;
    private List<String> roles;
    private Boolean active;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "action='" + action + '\'' +
                ", publicId='" + publicId + '\'' +
                ", roles=" + roles +'\'' +
                ", name='" + name + '\'' +
                ", active='" + active + '\'' +
                '}';
    }
}
