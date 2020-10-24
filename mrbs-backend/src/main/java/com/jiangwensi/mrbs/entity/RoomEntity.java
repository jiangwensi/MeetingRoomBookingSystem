package com.jiangwensi.mrbs.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jiang Wensi on 15/8/2020
 */
@Entity
@Table(name = "room",uniqueConstraints = {@UniqueConstraint(columnNames = {"name","org_id"})})
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String publicId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false, length = 255)
    private String facilities;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "room")
    private List<BookingEntity> bookings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "org_id")
    private OrganizationEntity organization;

    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<RoomImageEntity> roomImages;

    @ManyToMany
    @JoinTable(
            name = "room_admin",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(name="room_admin_unique_index",columnNames = {"room_id","user_id"})
    )
    private List<UserEntity> admins;

    @ManyToMany
    @JoinTable(
            name = "room_user",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(name="room_user_unique_index",columnNames = {"room_id","user_id"})
    )
    private List<UserEntity> users;

    @OneToMany(mappedBy="room",cascade = CascadeType.ALL)
    private List<BlockedTimeslotEntity> blockedTimeslots;


    public RoomEntity() {
        this.publicId = UUID.randomUUID().toString();
    }

    public RoomEntity(String publicId) {
        this.publicId = publicId;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<BookingEntity> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingEntity> bookings) {
        this.bookings = bookings;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public List<RoomImageEntity> getRoomImages() {
        return roomImages;
    }

    public void setRoomImages(List<RoomImageEntity> roomImages) {
        this.roomImages = roomImages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (name != null) {
            this.name = name.toUpperCase();
        }
        this.name = name;
    }

    public List<UserEntity> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserEntity> admins) {
        this.admins = admins;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public List<BlockedTimeslotEntity> getBlockedTimeslots() {
        return blockedTimeslots;
    }

    public void setBlockedTimeslots(List<BlockedTimeslotEntity> blockedTimeslots) {
        this.blockedTimeslots = blockedTimeslots;
    }
}
