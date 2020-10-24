package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.dto.OrganizationDto;
import com.jiangwensi.mrbs.dto.RoomDto;
import com.jiangwensi.mrbs.dto.UserDto;
import com.jiangwensi.mrbs.enumeration.RoleName;
import com.jiangwensi.mrbs.exception.AccessDeniedException;
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
import com.jiangwensi.mrbs.service.UserService;
import com.jiangwensi.mrbs.utils.MyModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private OrgService orgService;
    @Autowired
    private UserService userService;

    @GetMapping
//    @PreAuthorize("hasAuthority('SYSADM')")
    public SearchOrganizationResponse searchOrganization(@RequestParam(required = false) String name,
                                                         @RequestParam(required = false)  Boolean active){

        log.info("listOrganization() name:"+name+",active="+active);

//        if(!hasAuthorizedRole(RoleName.SYSADM.getName()) && !isOwningSomeOrganization()){
//            throw new AccessDeniedException("You are not allowed to search organization ");
//        };

        List<OrganizationDto> organizationDtos = orgService.search(name,active);

//        if(!hasAuthorizedRole(RoleName.SYSADM.getName()) && isOwningSomeOrganization()){
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            List<String> orgsOwnedByMeStr = userService.findUserByEmail(auth.getName()).getIsAdminOfOrganizations();
//            List<OrganizationDto> orgsOwnedByMe = orgsOwnedByMeStr.stream().map(o->orgService.viewOrganization(o)).collect(Collectors.toList());
//            organizationDtos = orgsOwnedByMe.stream().filter(organizationDtos::contains).collect(Collectors.toList());
//        }

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
        log.info("viewOrganization() publicId:"+publicId);

        if(!hasAuthorizedRoleOrAccessingMyOrganization(RoleName.SYSADM.getName(),publicId)){
            throw new AccessDeniedException("You are not allowed to view organization "+publicId);
        };

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
        String name = request.getName();
        String description = request.getDescription();
        Boolean active = request.isActive();
        List<String> admins = request.getAdmins();

        log.info("createOrganization name:"+name+",description:"+description+", admins:"+admins==null?"":String.join(
                " ",admins));

        OrganizationDto organizationDto = orgService.create(name,description,active,admins);
        OrganizationResponse returnValue = new OrganizationResponse();
        MyModelMapper.organizatioDtoToOrganizationResponseModelMapper().map(organizationDto,returnValue);

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Organization is created");
        return returnValue;
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('SYSADM')")
    public OrganizationResponse updateOrganization(@RequestBody OrganizationRequest request){
        String publicId = request.getPublicId();
        String name = request.getName();
        String description = request.getDescription();
        Boolean active = request.isActive();
        List<String> admins = request.getAdmins();

        log.info("updateOrganization ",request);

        OrganizationDto organizationDto = orgService.update(publicId,name,description,active, admins);
        OrganizationResponse returnValue = new OrganizationResponse();
        MyModelMapper.organizatioDtoToOrganizationResponseModelMapper().map(organizationDto,returnValue);

        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Organization is updated");
        return returnValue;
    }


    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasAuthority('SYSADM')")
    public GeneralResponse deleteOrganization(@PathVariable String publicId){
        log.info("deleteOrganization() publicId:"+publicId);

        orgService.delete(publicId);
        GeneralResponse returnValue = new GeneralResponse();
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Organization is deleted.");
        return returnValue;
    }

    @GetMapping("/{orgPublicId}/admins")
    public ListUserResponse listOrgAdmins(@PathVariable String orgPublicId){
        if(!hasAuthorizedRoleOrAccessingMyOrganization(RoleName.SYSADM.getName(),orgPublicId)){
            throw new AccessDeniedException("You are not allowed to view organization "+orgPublicId);
        };
        ListUserResponse returnValue = new ListUserResponse();
        List<UserDto> users = orgService.listAdminByOrg(orgPublicId);
        List<UserResponseItem> userResponseItems = new ModelMapper().map(users,new TypeToken<List<UserResponseItem>>(){}.getType());
        returnValue.setUsers(userResponseItems);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }

    @GetMapping("/{orgPublicId}/rooms")
    public SearchRoomResponse listOrgRooms(@PathVariable String orgPublicId){
        if(!hasAuthorizedRoleOrAccessingMyOrganization(RoleName.SYSADM.getName(),orgPublicId)){
            throw new AccessDeniedException("You are not allowed to view organization "+orgPublicId);
        };
        SearchRoomResponse returnValue = new SearchRoomResponse();
        List<RoomDto> rooms = orgService.listRoomsByOrg(orgPublicId);
        List<SearchRoomResponseItem> roomResponseItems = new ModelMapper().map(rooms,
                new TypeToken<List<SearchRoomResponseItem>>(){}.getType());
        returnValue.setRooms(roomResponseItems);
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }


    private boolean hasAuthorizedRoleOrAccessingMyOrganization(String authorizedRole,String orgPublicId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) auth.getAuthorities();
        for(GrantedAuthority ga: grantedAuthorities){
            if(ga.getAuthority().equals(authorizedRole)){
                log.info(authorizedRole+"is authorized");
                return true;
            }
        }

        OrganizationDto organizationDto = orgService.viewOrganization(orgPublicId);
        List<String> admins = organizationDto.getAdmins();
        UserDto userDto = userService.findUserByEmail(auth.getName());
        for(String admin: admins){
            if(userDto.getPublicId().equals(admin)){
                log.info("accessing my organization");
                return true;
            }
        }
        return false;
    }

    private boolean hasAuthorizedRole(String authorizedRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) auth.getAuthorities();
        for(GrantedAuthority ga: grantedAuthorities){
            if(ga.getAuthority().equals(authorizedRole)){
                log.info(authorizedRole+"is authorized");
                return true;
            }
        }
        return false;
    }
    private boolean isOwningSomeOrganization() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> myOrgs = userService.findUserByEmail(auth.getName()).getIsAdminOfOrganizations();
        if(myOrgs!=null && myOrgs.size()>0){
            return true;
        }
        return false;
    }
}
