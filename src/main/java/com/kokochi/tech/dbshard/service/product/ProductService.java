package com.kokochi.tech.dbshard.service.product;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Long insertProduct(Product product) {
        product.setRegDate(LocalDateTime.now());
        return upsertProduct(product);
    }

    @Transactional
    public Long upsertProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return savedProduct.getProductId();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProductList() {
        return productRepository.findAll();
    }

    // 검색 기능 추가
    // 페이징
    // 다양한 조건 필터링
}
