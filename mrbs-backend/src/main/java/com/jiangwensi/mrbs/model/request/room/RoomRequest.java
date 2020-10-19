package com.jiangwensi.mrbs.model.request.room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 25/8/2020
 */
public class RoomRequest {

    private String publicId;
    private String name;
    private int capacity;
    private String facilities;
    private String description;
    private boolean active;
    private List<String> users = new ArrayList<>();
    private List<String> admins = new ArrayList<>();
    private String orgPublicId;
    private List<BlockedTimeSlot> blockedTimeslots = new ArrayList<>();
//    private MultipartFile roomImage;

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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public String getOrgPublicId() {
        return orgPublicId;
    }

    public void setOrgPublicId(String orgPublicId) {
        this.orgPublicId = orgPublicId;
    }

    public List<BlockedTimeSlot> getBlockedTimeslots() {
        return blockedTimeslots;
    }

    public void setBlockedTimeslots(List<BlockedTimeSlot> blockedTimeslots) {
        this.blockedTimeslots = blockedTimeslots;
    }


    //    public InputStream getRoomImage() {
//        return roomImage;
//    }
//
//    public void setRoomImage(InputStream roomImage) {
//        this.roomImage = roomImage;
//    }

    @Override
    public String toString() {
        return "RoomRequest{" +
                "publicId='" + publicId + '\'' +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", facilities='" + facilities + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", bookings=" + users +
                ", admins=" + admins +
                ", organization='" + orgPublicId + '\'' +
                '}';
    }
}
