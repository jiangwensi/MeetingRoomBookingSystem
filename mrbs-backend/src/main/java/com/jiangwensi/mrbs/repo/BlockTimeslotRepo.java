package com.jiangwensi.mrbs.repo;

import com.jiangwensi.mrbs.entity.BlockedTimeslotEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Jiang Wensi on 3/9/2020
 */
public interface BlockTimeslotRepo extends CrudRepository<BlockedTimeslotEntity,Long> {
}
