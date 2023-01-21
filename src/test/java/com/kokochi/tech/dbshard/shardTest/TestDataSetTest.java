package com.kokochi.tech.dbshard.shardTest;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.product.ProductImgScore;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductImgType;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductSeasonType;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductType;
import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgScoreRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Transactional
@Rollback(false)
public class TestDataSetTest extends AbstractShardTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductImgRepository productImgRepository;
    @Autowired
    ProductImgScoreRepository productImgScoreRepository;

    // 사용자 추가
//    @Test
    void 사용자_추가() {
        String[] names = {"junu", "ddolbok", "kuiki", "yummy", "narang", "bbibu", "jjanu"};
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            String name = names[i % names.length];
            userList.add(User.createUser(name + "_" + i, "1234"));
        }
        userRepository.saveAll(userList);
    }

    // 작품 추가
//    @Test
    void 작품_추가() {
        String[] names = {"rust", "lol", "rainbowsix", "balorant", "genshin", "zelda", "honkai"};
        ProductType[] productTypes = ProductType.values();
        ProductSeasonType[] seasonTypes = ProductSeasonType.values();
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            String name = names[i % names.length];
            ProductType productType = productTypes[i % productTypes.length];
            ProductSeasonType seasonType = seasonTypes[i % seasonTypes.length];
            productList.add(Product.createProduct(productType, seasonType, name + "_" + i, "director"));
        }
        productRepository.saveAll(productList);
    }

    // 이미지 추가
//    @Test
    void 이미지_추가() {
        String[] names = {"bocchi", "nijika", "ryo", "kita", "anya", "akebi", "biwa"};
        List<Product> productList = productRepository.findAll();
        List<User> userList = userRepository.findAll();
        ProductImgType[] imgTypes = ProductImgType.values();
        List<ProductImg> productImgList = new ArrayList<>();

        for (Product product : productList) {
            for (int i = 0; i < 1000; i++) {
                String name = names[i % names.length];
                ProductImgType imgType = imgTypes[i % imgTypes.length];
                User user = userList.get(i % userList.size());
                productImgList.add(ProductImg.createProductImg(product, imgType, name + "_" + i, "test", user));
            }
        }
        productImgRepository.saveAll(productImgList);
    }

    // 이미지 평가 추가
//    @Test
    void 이미지_평가_추가() {
        List<User> userList = userRepository.findAll();
        List<ProductImg> productImgList = productImgRepository.findAll();

        System.out.println("TEST :: size = " + productImgList.size());
        Random random = new Random();
        int max = 999;
        int min = 1;
        List<ProductImgScore> productImgScoreList = new ArrayList<>();

        for (int i=0;i<productImgList.size();i++) {
            for (int j=0;j<userList.size();j++) {
                if( ((i * j) + j) % 979 == 0 ) {
                    int score = random.nextInt((max - min) + 1) + min;
                    productImgScoreRepository.save(ProductImgScore.builder()
                            .productImgId(productImgList.get(i).getProductImgId())
                            .userId(userList.get(j).getUserId())
                            .score(score)
                            .build());
                }
            }
        }
    }

    // 데이터 셋 초기화
//    @Test
    void 데이터셋_초기화() {
//        userRepository.deleteAll();
//        productRepository.deleteAll();
        productImgRepository.deleteAll();
//        productImgScoreRepository.deleteAll();
    }

}
