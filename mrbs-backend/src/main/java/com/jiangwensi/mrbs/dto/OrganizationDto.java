package com.jiangwensi.mrbs.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 16/8/2020
 */

public class OrganizationDto {
    private String publicId;
    private String name;
    private List<String> admins = new ArrayList<>();
    private List<String> rooms = new ArrayList<>();
    private boolean active;
    private String description;

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

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationDto that = (OrganizationDto) o;

        return publicId != null ? publicId.equals(that.publicId) : that.publicId == null;
    }

    @Override
    public int hashCode() {
        return publicId != null ? publicId.hashCode() : 0;
    }
}
