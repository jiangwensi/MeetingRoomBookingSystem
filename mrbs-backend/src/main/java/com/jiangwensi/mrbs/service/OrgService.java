package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.dto.OrganizationDto;
import com.jiangwensi.mrbs.dto.RoomDto;
import com.jiangwensi.mrbs.dto.UserDto;

import java.util.List;

/**
 * Created by Jiang Wensi on 24/8/2020
 */
public interface OrgService {
    List<OrganizationDto> search(String name, Boolean status);
    OrganizationDto viewOrganization(String publicId);
    OrganizationDto create(String name, String description, Boolean status,List<String> admins);
    OrganizationDto update(String publicId,String name, String description, Boolean status,List<String> admins);
    void delete(String publicId);

    List<UserDto> listAdminByOrg(String orgPublicId);

    List<RoomDto> listRoomsByOrg(String orgPublicId);
}
