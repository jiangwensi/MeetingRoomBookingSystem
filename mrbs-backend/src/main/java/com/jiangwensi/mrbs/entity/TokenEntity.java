package com.jiangwensi.mrbs.entity;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.persistence.*;

/**
 * Created by Jiang Wensi on 15/8/2020
 */
@Entity
@Table(name = "token",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","type"}))
@Slf4j
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(
    //        fetch = FetchType.EAGER
    )
    @JoinColumn(name="user_id")
    private UserEntity user;

    @Column(nullable = false,length = 255)
    private String token;

    @Column(nullable=false, length=30)
    private String type;

    private String returnUrl;

//    @PreRemove
//    private void preRemove() {
//        log.debug("preRemove()");
//
//        user.getTokens().remove(this);
//    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public static Logger getLog() {
        return log;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
