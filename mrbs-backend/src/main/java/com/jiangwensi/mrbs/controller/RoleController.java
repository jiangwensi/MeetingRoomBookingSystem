package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.constant.PathConst;
import com.jiangwensi.mrbs.model.response.RoleResponse;
import com.jiangwensi.mrbs.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Jiang Wensi on 27/8/2020
 */
@RestController
@Slf4j
@RequestMapping(PathConst.ROLE_PATH)
@PreAuthorize("hasAuthority('SYSADM')")
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Scope("request")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public RoleResponse listRoles(){
        RoleResponse returnValue = new RoleResponse();
        List<String> roles = roleService.listAllRoles();
        returnValue.setRoles(roles);
        returnValue.setMessage("List role is successful");
        returnValue.setStatus(MyResponseStatus.success.name());
        return returnValue;
    }
}
