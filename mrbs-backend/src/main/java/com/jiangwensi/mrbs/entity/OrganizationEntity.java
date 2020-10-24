package com.jiangwensi.mrbs.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 15/8/2020
 */
@Entity
@Table(name = "org")
public class OrganizationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String publicId;

    @Column(nullable = false, length = 200, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "org_admin",
            joinColumns = @JoinColumn(name = "org_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> admins = new ArrayList<>();

    @OneToMany(mappedBy = "organization")
    private List<RoomEntity> rooms = new ArrayList<>();

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, length = 255)
    private String description;

    public OrganizationEntity() {
    }

    public OrganizationEntity(String publicId) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (name != null) {
            this.name = name.toUpperCase();
        } else {
            this.name = name;
        }
    }

    public List<UserEntity> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserEntity> admins) {
        this.admins = admins;
    }

    public List<RoomEntity> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomEntity> rooms) {
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
}
