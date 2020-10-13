package com.jiangwensi.mrbs.entity;

import javax.persistence.*;

/**
 * Created by Jiang Wensi on 15/8/2020
 */
@Entity
@Table(name = "avatar")
public class AvatarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @Lob
    private Byte[] avatar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(Byte[] avatar) {
        this.avatar = avatar;
    }
}
