package com.jiangwensi.mrbs.model.request.org;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 24/8/2020
 */
public class  OrganizationRequest {

    private String publicId;
    private String name;
    private String description;
    private boolean active;
    private List<String> admins = new ArrayList<>();

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

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "OrganizationRequest{" +
                "publicId='" + publicId + '\'' +
                ", name='" + name + '\'' +
                ", admins=" + admins +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }
}
