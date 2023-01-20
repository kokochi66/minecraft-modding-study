package com.kokochi.tech.dbshard.scenario;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductSeasonType;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductType;
import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.product.ProductImgService;
import com.kokochi.tech.dbshard.service.product.ProductService;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class ProductScenarioTest extends AbstractScenarioTest {

    @Autowired private ProductService productService;
    @Autowired private ProductRepository productRepository;

    // 작품 생성
    @Test
    void 작품_생성() {
        Product product = productService.upsertProduct(Product.createProduct(ProductType.GAME, ProductSeasonType.SPRING_20, "sakura moyu", "a"));

        Product findProduct = productService.getProductById(product.getProductId());
        assertThat(findProduct.getProductTitle()).isEqualTo(product.getProductTitle());
    }

    // 작품 제거
    @Test
    @Rollback(false)
    void 작품_제거() {
        Product product = productService.upsertProduct(Product.createProduct(ProductType.GAME, ProductSeasonType.SPRING_20, "sakura moyu", "a"));
        productService.deleteProduct(product);

        Product findProduct = productService.getProductById(product.getProductId());
        assertThat(findProduct.getProductTitle()).isEqualTo(product.getProductTitle());
    }


    // 작품 전체 조회
    @Test
    void 작품_전체조회() {
        List<Product> productList = productService.getProductList();
        for (Product product : productList) {
            System.out.println("TEST :: product = " + product);
        }
    }

    // 작품 시즌 별 조회 (페이징)
    @Test
    void 작품_시즌별_조회() {
        Page<Product> page = productService.getProductByProductSeasonPage(ProductSeasonType.FALL_22, 10, 0);

        System.out.println("TEST :: totalElement = " + page.getTotalElements());
        System.out.println("TEST :: number = " + page.getNumber());
        System.out.println("TEST :: totalpages = " + page.getTotalPages());

        for (Product product : page) {
            System.out.println("TEST :: prodcut = " + product);
        }
        System.out.println("TEST :: hasNext = " + page.hasNext());
    }

    // 작품 타입 별 조회 (페이징)
    @Test
    void 작품_타입별_조회() {
        Page<Product> page = productService.getProductByProductTypePage(ProductType.ANIME, 10, 0);

        System.out.println("TEST :: totalElement = " + page.getTotalElements());
        System.out.println("TEST :: number = " + page.getNumber());
        System.out.println("TEST :: totalpages = " + page.getTotalPages());

        for (Product product : page) {
            System.out.println("TEST :: prodcut = " + product);
        }
        System.out.println("TEST :: hasNext = " + page.hasNext());
    }

    // 작품 이름 별 조회 (페이징)
    @Test
    void 작품_이름별_조회() {
        Page<Product> page = productService.getProductByProductTitlePage("sakura", 10, 0);

        System.out.println("TEST :: totalElement = " + page.getTotalElements());
        System.out.println("TEST :: number = " + page.getNumber());
        System.out.println("TEST :: totalpages = " + page.getTotalPages());

        for (Product product : page) {
            System.out.println("TEST :: prodcut = " + product);
        }
        System.out.println("TEST :: hasNext = " + page.hasNext());
    }


    // 많은 작품 추가
//    @Test
    @Rollback(false)
    void 작품_대량추가() {
        Random random = new Random();
        ProductType[] typeArr = ProductType.values();
        ProductSeasonType[] seasonArr = ProductSeasonType.values();
        String[] titleArr = {"bocchi", "sakura", "dacapo", "nine"};
        int max = 9999;
        int min = 1;

        List<Product> insertList = new ArrayList<>();

        for(int i=0;i<1000;i++) {
            int randomSet = random.nextInt((max - min) + 1) + min;
            ProductType productType = typeArr[randomSet % typeArr.length];
            ProductSeasonType productSeasonType = seasonArr[randomSet % seasonArr.length];
            String title = titleArr[randomSet % titleArr.length];
            insertList.add(Product.createProduct(productType, productSeasonType, title+"_"+i, "kokochi"));
        }

        productRepository.saveAll(insertList);

    }
}
