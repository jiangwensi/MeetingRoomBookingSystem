package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.dto.OrganizationDto;
import com.jiangwensi.mrbs.dto.RoomDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.model.request.org.OrganizationRequest;
import com.jiangwensi.mrbs.model.response.GeneralResponse;
import com.jiangwensi.mrbs.model.response.organization.OrganizationResponse;
import com.jiangwensi.mrbs.model.response.organization.SearchOrganizationResponse;
import com.jiangwensi.mrbs.model.response.organization.SearchOrganizationResponseItem;
import com.jiangwensi.mrbs.model.response.room.SearchRoomResponse;
import com.jiangwensi.mrbs.model.response.room.SearchRoomResponseItem;
import com.jiangwensi.mrbs.model.response.user.ListUserResponse;
import com.jiangwensi.mrbs.model.response.user.UserResponseItem;
import com.jiangwensi.mrbs.service.OrgService;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Jiang Wensi on 17/8/2020
 */
@RestController
@RequestMapping(PathConst.ORGANIZATION_PATH)
@Slf4j
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Scope("request")
public class OrgController {

    private OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('SYSADM')")
    public SearchOrganizationResponse searchOrganization(
            @RequestParam(required = false) String name,
            @RequestParam(required = false)  Boolean active){

        List<OrganizationDto> organizationDtos = orgService.search(name,active);
        SearchOrganizationResponse returnValue = new SearchOrganizationResponse();

        organizationDtos.forEach(o-> {
            SearchOrganizationResponseItem organization = new SearchOrganizationResponseItem();
            MyModelMapper.organizationDtoToOrganizationSearchResponseItemModelMapper().map(o,organization);
            returnValue.getOrganizations().add(organization);
        });
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Search is successful");
        return returnValue;
    }

    @GetMapping("/{publicId}")
    public OrganizationResponse viewOrganization(@PathVariable String publicId){

        OrganizationDto organizationDto = orgService.viewOrganization(publicId);
        OrganizationResponse returnValue = new OrganizationResponse();
        MyModelMapper.organizatioDtoToOrganizationResponseModelMapper().map(organizationDto,returnValue);
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("View is successful");
        return returnValue;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SYSADM')")
    public OrganizationResponse createOrganization(@RequestBody OrganizationRequest request){

        OrganizationDto organizationDto = orgService.create(request);
        OrganizationResponse returnValue = new OrganizationResponse();
        MyModelMapper.organizatioDtoToOrganizationResponseModelMapper().map(organizationDto,returnValue);

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Organization is created");
        return returnValue;
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('SYSADM')")
    public OrganizationResponse updateOrganization(@RequestBody OrganizationRequest request){

        OrganizationDto organizationDto = orgService.update(request);
        OrganizationResponse returnValue = new OrganizationResponse();
        MyModelMapper.organizatioDtoToOrganizationResponseModelMapper().map(organizationDto,returnValue);

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Organization is updated");
        return returnValue;
    }


    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasAuthority('SYSADM')")
    public GeneralResponse deleteOrganization(@PathVariable String publicId){

        orgService.delete(publicId);
        GeneralResponse returnValue = new GeneralResponse();
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Organization is deleted.");
        return returnValue;
    }

    @GetMapping("/{orgPublicId}/admins")
    public ListUserResponse listOrgAdmins(@PathVariable String orgPublicId){
        ListUserResponse returnValue = new ListUserResponse();
        List<UserDto> users = orgService.listAdminByOrg(orgPublicId);
        List<UserResponseItem> userResponseItems = new ModelMapper().map(users,new TypeToken<List<UserResponseItem>>(){}.getType());
        returnValue.setUsers(userResponseItems);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }

    @GetMapping("/{orgPublicId}/rooms")
    public SearchRoomResponse listOrgRooms(@PathVariable String orgPublicId){
        SearchRoomResponse returnValue = new SearchRoomResponse();
        List<RoomDto> rooms = orgService.listRoomsByOrg(orgPublicId);
        List<SearchRoomResponseItem> roomResponseItems = new ModelMapper().map(rooms,
                new TypeToken<List<SearchRoomResponseItem>>(){}.getType());
        returnValue.setRooms(roomResponseItems);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }
}
