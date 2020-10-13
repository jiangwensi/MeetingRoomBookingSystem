package com.jiangwensi.mrbs.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 15/8/2020
 */
@Entity
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany(
            //        fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> users = new ArrayList<>();

    public RoleEntity() {
    }

    public RoleEntity(String name) {
        this.name = name;
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

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}
