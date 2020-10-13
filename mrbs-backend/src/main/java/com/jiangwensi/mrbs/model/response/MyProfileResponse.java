package com.jiangwensi.mrbs.model.response;

import java.util.List;

/**
 * Created by Jiang Wensi on 24/8/2020
 */
public class MyProfileResponse extends GeneralResponse {
    private String publicId;
    private String name;
    private String email;
    private boolean active;
    private List<String> isAdminOfRooms;
    private List<String> isAdminOfOrganizations;
    private List<String> roles;
}
