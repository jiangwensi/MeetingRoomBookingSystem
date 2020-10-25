package com.jiangwensi.mrbs.service;

import com.jiangwensi.mrbs.entity.RoleEntity;
import com.jiangwensi.mrbs.repo.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 27/8/2020
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<String> listAllRoles() {
      List<String> returnValue = new ArrayList<>();
      List<RoleEntity> roleEntities = IteratorUtils.toList(roleRepository.findAll().iterator());
      if(roleEntities!=null && roleEntities.size()>0){
          roleEntities.forEach(e->{
              returnValue.add(e.getName());
          });
      }
      return returnValue;
    };
}
