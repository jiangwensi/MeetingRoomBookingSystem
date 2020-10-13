package com.jiangwensi.mrbs.repo;

import com.jiangwensi.mrbs.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jiang Wensi on 17/8/2020
 */
public interface TokenRepository extends JpaRepository<TokenEntity,Long> {

    TokenEntity findByToken(String token);
    void deleteById(long id);
    void deleteByToken(String token);


    void deleteByUserIdAndType(long id, String name);

    TokenEntity findByUserIdAndType(long id, String name);

//    TokenEntity findByEmailAndType();
}
