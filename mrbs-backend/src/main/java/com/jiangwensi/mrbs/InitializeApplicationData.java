package com.jiangwensi.mrbs;

import com.jiangwensi.mrbs.entity.RoleEntity;
import com.jiangwensi.mrbs.enumeration.RoleName;
import com.jiangwensi.mrbs.repo.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Component
public class InitializeApplicationData {

    Logger logger = LoggerFactory.getLogger(InitializeApplicationData.class);

    @Autowired
    RoleRepository roleRepository;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event){
        logger.debug("onApplicationEvent is called");
        createRoles();
    }

    private void createRoles(){
        List<RoleEntity> roleEntities = new ArrayList<>();
        if (roleRepository.findByName(RoleName.USER.getName())==null) {
            roleEntities.add(new RoleEntity(RoleName.USER.toString()));
        }
        if (roleRepository.findByName(RoleName.SYSADM.getName())==null) {
            roleEntities.add(new RoleEntity(RoleName.SYSADM.toString()));
        }
        roleRepository.saveAll(roleEntities);
    }

}
