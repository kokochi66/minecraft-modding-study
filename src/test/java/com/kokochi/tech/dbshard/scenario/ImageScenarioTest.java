package com.kokochi.tech.dbshard.scenario;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.product.ProductImgScore;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductImgType;
import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.product.ProductImgService;
import com.kokochi.tech.dbshard.service.product.ProductService;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgScoreRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Transactional
public class ImageScenarioTest extends AbstractScenarioTest {

    @Autowired ProductService productService;
    @Autowired ProductImgService productImgService;
    @Autowired ProductImgRepository productImgRepository;
    @Autowired ProductImgScoreRepository productImgScoreRepository;
    @Autowired UserRepository userRepository;

    // 사용자가 올린 이미지 조회 (페이징)
    @Test
    void 이미지_사용자별_조회() {
        User user = userRepository.findById("").orElseThrow();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductImg> res = productImgRepository.findByUploadUserHotListPaging(user, pageRequest);
        for (ProductImg img : res) {
            System.out.println("TEST :: img = " + img);
        }
    }

    // 인기 이미지 조회 (페이징)
    @Test
    void 이미지_인기별_조회() {
        User user = userRepository.findById("").orElseThrow();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductImg> res = productImgRepository.findByHotListPaging(pageRequest);
        for (ProductImg img : res) {
            System.out.println("TEST :: img = " + img);
        }
    }

//     이미지 평가 저장
    @Rollback(false)
    void 이미지_평가_다량저장() {
        List<ProductImg> productImgList = productImgRepository.findAll();
        List<User> userList = userRepository.findAll();
        Random random = new Random();
        int max = 100;
        int min = 1;
        long i = 0;

        List<ProductImgScore> scoreList = new ArrayList<>();
        for (ProductImg productImg : productImgList) {
            if (i++ % 79 == 0) {
                for (User user : userList) {
                    scoreList.add(
                            ProductImgScore.builder()
                                    .userId(user.getUserId())
                                    .productImgId(productImg.getProductImgId())
                                    .score(random.nextInt((max - min) + 1) + min)
                                    .build());
                }
            }
        }
        productImgScoreRepository.saveAll(scoreList);
    }


    @Test
    @Rollback(false)
    void 이미지_다량_저장() {
        List<Product> productList = productService.getProductList();
        ProductImgType[] imgtypeArr = ProductImgType.values();
        String[] titleArr = {"chamcham2", "jinu", "nanayang", "leechunhyang", "mamwa", "bbibbu", "gambler"};
        List<User> users = userRepository.findAll();

        List<ProductImg> productImgList = new ArrayList<>();

        for (Product product : productList) {
            for (int i=0;i<10;i++) {
                ProductImgType productImgType = imgtypeArr[i % imgtypeArr.length];
                String title = titleArr[i % titleArr.length];
                User user = users.get(i % users.size());
                productImgList.add(ProductImg.createProductImg(product, productImgType, title, "test", user));
            }

        }
        productImgRepository.saveAll(productImgList);

    }


}
