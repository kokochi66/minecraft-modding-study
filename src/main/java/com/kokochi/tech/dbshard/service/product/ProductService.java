package com.kokochi.tech.dbshard.service.product;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductSeasonType;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductType;
import com.kokochi.tech.dbshard.service.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product insertProduct(Product product) {
        product.setRegDate(LocalDateTime.now());
        return upsertProduct(product);
    }

    @Transactional
    public Product upsertProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> getProductList() {
        return productRepository.findAll();
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    // 작품 타입별 조회
    public Page<Product> getProductByProductTypePage(ProductType productType, int size, int offset) {
        PageRequest pageRequest = PageRequest.of(offset, size, Sort.by(Sort.Direction.ASC, "productTitle"));
        return productRepository.findByProductType(productType, pageRequest);
    }

    // 작품 시즌별 조회
    public Page<Product> getProductByProductSeasonPage(ProductSeasonType productSeasonType, int size, int offset) {
        PageRequest pageRequest = PageRequest.of(offset, size, Sort.by(Sort.Direction.ASC, "productTitle"));
        return productRepository.findByProductSeasonType(productSeasonType, pageRequest);
    }

    // 작품 이름 조회
    public Page<Product> getProductByProductTitlePage(String productTitle, int size, int offset) {
        PageRequest pageRequest = PageRequest.of(offset, size, Sort.by(Sort.Direction.ASC, "productTitle"));
        return productRepository.findByProductTitleContains(productTitle, pageRequest);
    }
}
