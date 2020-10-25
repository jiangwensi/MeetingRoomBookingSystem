package com.jiangwensi.mrbs.repo;

import com.jiangwensi.mrbs.entity.RoomEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface RoomRepository extends CrudRepository<RoomEntity,Long> {
    @Query(nativeQuery = true,
    value =
            "select * from room " +
                    "where name like concat('%',ifnull(:name,''),'%') " +
                    "and org_id in (select id from org where name like concat('%',ifnull(:orgName," +
                    "''),'%')) "+
                    "and (active =ifnull(:active,true) " +
                        "or active = ifnull(:active,false))"
    )
    List<RoomEntity> searchRoom(@Param("name") String name,
                                @Param("orgName") String orgName, @Param("active") Boolean active);

    
    RoomEntity findByPublicId(String publicId);

    @Query(nativeQuery = true,
            value = "select r.* from room r join org o on r.org_id = o.id and o.public_id = :orgPublicId and r.name =" +
                    " :roomName")
    RoomEntity findByRoomNameAndOrgPublicId(@Param("roomName") String roomName, @Param("orgPublicId") String orgPublicId);

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Query(nativeQuery = true,
    value = "delete from room where org_id in (select id from org where id = :orgId )")
    void deleteByOrgId(@Param("orgId") Long orgId);

}
