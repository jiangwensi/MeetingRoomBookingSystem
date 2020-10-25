package com.jiangwensi.mrbs.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jiang Wensi on 15/8/2020
 */
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String publicId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 255,unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private boolean emailVerified = true;

    @Column(nullable = false, length = 255)
    private boolean active = true;

    @OneToOne(mappedBy = "user"
            //,fetch = FetchType.LAZY
    )
    private AvatarEntity avatar;

    @OneToMany(mappedBy = "user"
            , cascade = CascadeType.ALL
    )
    private List<TokenEntity> tokens = new ArrayList<>();

    @ManyToMany(mappedBy = "users"
                    ,fetch = FetchType.EAGER
    )
    private List<RoleEntity> roles = new ArrayList<>();

    @OneToMany(mappedBy = "bookedBy")
    private List<BookingEntity> bookings = new ArrayList<>();

    @ManyToMany(mappedBy = "admins")
    private List<OrganizationEntity> isAdminOfOrganizations = new ArrayList<>();

    @ManyToMany(mappedBy = "admins")
    private List<RoomEntity> isAdminOfRooms = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private List<RoomEntity> isUserOfRooms = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(String publicId) {
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

        if(name!=null){
            this.name=name.toUpperCase();
        } else {
            this.name = name;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email.toUpperCase();
        } else {
            this.email = email;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean active) {
        this.emailVerified = active;
    }

    public AvatarEntity getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarEntity avatar) {
        this.avatar = avatar;
    }

    public List<TokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokenEntity> tokens) {
        this.tokens = tokens;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public List<BookingEntity> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingEntity> bookings) {
        this.bookings = bookings;
    }

    public List<OrganizationEntity> getIsAdminOfOrganizations() {
        return isAdminOfOrganizations;
    }

    public void setIsAdminOfOrganizations(List<OrganizationEntity> isAdminOfOrganizations) {
        this.isAdminOfOrganizations = isAdminOfOrganizations;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<RoomEntity> getIsAdminOfRooms() {
        return isAdminOfRooms;
    }

    public void setIsAdminOfRooms(List<RoomEntity> isAdminOfRooms) {
        this.isAdminOfRooms = isAdminOfRooms;
    }

    public List<RoomEntity> getIsUserOfRooms() {
        return isUserOfRooms;
    }

    public void setIsUserOfRooms(List<RoomEntity> isUserOfRooms) {
        this.isUserOfRooms = isUserOfRooms;
    }
}
