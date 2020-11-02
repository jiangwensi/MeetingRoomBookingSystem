package com.jiangwensi.mrbs.repo;

import com.jiangwensi.mrbs.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Created by Jiang Wensi on 17/8/2020
 */
public interface TokenRepository extends JpaRepository<TokenEntity,Long> {

    TokenEntity findByToken(String token);
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void deleteById(long id);
    void deleteByToken(String token);
    TokenEntity findByUserIdAndType(long id, String name);
}
