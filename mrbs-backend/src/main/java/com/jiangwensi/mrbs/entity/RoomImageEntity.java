package com.jiangwensi.mrbs.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Jiang Wensi on 15/8/2020
 */
@Entity
@Table(name = "room_image")
public class RoomImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="room_id")
    private RoomEntity room;

    @Lob
    private byte[] image;

    @Column(nullable = false)
    private String publicId;

    public RoomImageEntity() {
        this.publicId = UUID.randomUUID().toString();
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

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
