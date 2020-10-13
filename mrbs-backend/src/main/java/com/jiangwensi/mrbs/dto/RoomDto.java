package com.jiangwensi.mrbs.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
public class RoomDto {
    private String publicId;
    private String name;
    private int capacity;
    private String facilities;
    private String description;
    private boolean active;
    private List<String> bookings = new ArrayList<>();
    private List<String> admins = new ArrayList<>();
    private String organization;
    private RoomImageDto roomImage;
    private List<String> users = new ArrayList<>();
    private List<BlockedTimeslotDto> blockedTimeslots = new ArrayList<>();

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
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

    public List<String> getBookings() {
        return bookings;
    }

    public void setBookings(List<String> bookings) {
        this.bookings = bookings;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public RoomImageDto getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(RoomImageDto roomImage) {
        this.roomImage = roomImage;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<BlockedTimeslotDto> getBlockedTimeslots() {
        return blockedTimeslots;
    }

    public void setBlockedTimeslots(List<BlockedTimeslotDto> blockedTimeslots) {
        this.blockedTimeslots = blockedTimeslots;
    }
}
