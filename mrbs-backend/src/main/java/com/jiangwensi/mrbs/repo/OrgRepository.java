package com.jiangwensi.mrbs.repo;

import com.jiangwensi.mrbs.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by Jiang Wensi on 24/8/2020
 */
public interface OrgRepository extends PagingAndSortingRepository<OrganizationEntity,Long> {

    OrganizationEntity findByPublicId(String publicId);

    void deleteByPublicId(String publicId);


    @Query(nativeQuery = true,
            value="select * from org " +
                    "where name like concat('%',:name,'%') " +
                    "and (:active is null ||  active=:active)")
    List<OrganizationEntity> findByNameAndActive(String name, Boolean active);
}
