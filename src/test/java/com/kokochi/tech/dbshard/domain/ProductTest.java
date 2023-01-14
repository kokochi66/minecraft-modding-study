package com.kokochi.tech.dbshard.domain;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.service.repository.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    @Transactional
    public void test() {
        productRepository.save(Product.builder()
                        .
                .build());

    }
}
