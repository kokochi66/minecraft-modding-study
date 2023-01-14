package com.kokochi.tech.dbshard.service.repository.product;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.domain.product.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

}
