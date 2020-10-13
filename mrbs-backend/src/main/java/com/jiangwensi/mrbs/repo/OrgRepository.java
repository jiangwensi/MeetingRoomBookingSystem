package com.jiangwensi.mrbs.repo;

import com.jiangwensi.mrbs.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Jiang Wensi on 24/8/2020
 */
public interface OrgRepository extends CrudRepository<OrganizationEntity,Long> {

    OrganizationEntity findByPublicId(String publicId);

    void deleteByPublicId(String publicId);

    @Query(nativeQuery = true, value = "select o.* from org o where o.name like %:name%")
    List<OrganizationEntity> findByNameLikeValue(@Param("name") String name);

    @Query(nativeQuery = true, value = "select * from org where name like %:name% and active = :active")
    List<OrganizationEntity> findByNameLikeAndActive(@Param("name") String name,@Param("active") boolean active);


    List<OrganizationEntity> findByActive(Boolean active);
}
