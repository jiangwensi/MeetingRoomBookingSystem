package com.jiangwensi.mrbs.repo;

import com.jiangwensi.mrbs.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    RoleEntity findByName(String role);
}
