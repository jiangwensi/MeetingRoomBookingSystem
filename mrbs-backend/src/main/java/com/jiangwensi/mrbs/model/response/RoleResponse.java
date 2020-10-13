package com.jiangwensi.mrbs.model.response;

import java.util.List;

/**
 * Created by Jiang Wensi on 27/8/2020
 */
public class RoleResponse extends GeneralResponse{
    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
