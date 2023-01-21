package com.kokochi.tech.dbshard.service.product.repository;

import com.kokochi.tech.dbshard.domain.product.ProductImgScore;
import com.kokochi.tech.dbshard.domain.product.ProductImgScorePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImgScoreRepository extends JpaRepository<ProductImgScore, ProductImgScorePK> {

    List<ProductImgScore> findAllByProductImgId(Long productImgId);

    List<ProductImgScore> findAllByUserId(Long userId);


}
