package com.jiangwensi.mrbs.model.response.room;

import com.jiangwensi.mrbs.model.request.room.BlockedTimeSlot;
import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public class RoomResponse extends GeneralResponse {

    private String publicId;
    private String name;
    private int capacity;
    private String facilities;
    private String description;
    private boolean active;
    private List<String> bookings = new ArrayList<>();
    private List<String> admins = new ArrayList<>();
    private List<String> users = new ArrayList<>();
    private List<BlockedTimeSlot> blockedTimeslots = new ArrayList<>();
    private String organization;

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

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<BlockedTimeSlot> getBlockedTimeslots() {
        return blockedTimeslots;
    }

    public void setBlockedTimeslots(List<BlockedTimeSlot> blockedTimeslots) {
        this.blockedTimeslots = blockedTimeslots;
    }
}
