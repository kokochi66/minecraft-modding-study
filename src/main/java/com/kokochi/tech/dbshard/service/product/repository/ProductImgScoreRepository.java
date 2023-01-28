package com.kokochi.tech.dbshard.service.product.repository;

import com.kokochi.tech.dbshard.domain.product.ProductImgScore;
import com.kokochi.tech.dbshard.domain.product.ProductImgScorePK;
import com.kokochi.tech.dbshard.shard.annotation.ShardingRepository;
import com.kokochi.tech.dbshard.shard.enumType.ShardingTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ShardingRepository(target = ShardingTarget.USER)
public interface ProductImgScoreRepository extends JpaRepository<ProductImgScore, ProductImgScorePK> {

    List<ProductImgScore> findAllByProductImgId(Long productImgId);

    List<ProductImgScore> findAllByUserId(Long userId);


}
