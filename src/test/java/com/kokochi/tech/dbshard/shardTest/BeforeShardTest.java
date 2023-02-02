package com.kokochi.tech.dbshard.shardTest;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.product.ProductImgScore;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductImgType;
import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.scenario.AbstractScenarioTest;
import com.kokochi.tech.dbshard.service.product.ProductImgSearchService;
import com.kokochi.tech.dbshard.service.product.ProductImgService;
import com.kokochi.tech.dbshard.service.product.ProductService;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgScoreRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductRepository;
import com.kokochi.tech.dbshard.service.user.UserService;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class BeforeShardTest extends AbstractScenarioTest {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImgService productImgService;
    @Autowired
    private ProductImgSearchService productImgSearchService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImgRepository productImgRepository;
    @Autowired
    private ProductImgScoreRepository productImgScoreRepository;

//
//    @Test
//    void 평가조회() {
//        List<ProductImgScore> all = productImgScoreRepository.findAll();
//        System.out.println("TEST :: size = " + all.size());
//    }

    // 인기 이미지 조회 (10개)
    @Test
    void 인기이미지_조회_10() {
        List<ProductImg> p = productImgSearchService.getProductImgListByHotList(10, 0);
//        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
//        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
//        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
//        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
        System.out.println("TEST :: p " + p.size());
    }

    // 인기 이미지 조회 (100개)
    @Test
    void 인기이미지_조회_100() {
        List<ProductImg> p = productImgSearchService.getProductImgListByHotList(100, 0);
//        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
//        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
//        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
//        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
        System.out.println("TEST :: p " + p.size());
    }

    // 인기 이미지 조회 (1000개)
    @Test
    void 인기이미지_조회_1000() {
        List<ProductImg> p = productImgSearchService.getProductImgListByHotList(1000, 0);
//        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
//        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
//        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
//        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
        System.out.println("TEST :: p " + p.size());
    }

    // 인기 이미지 조회 (전체)
    @Test
    void 인기이미지_조회_All() {
        List<ProductImg> p = productImgSearchService.getProductImgListByHotList(1000000, 0);
//        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
//        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
//        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
//        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
        System.out.println("TEST :: p " + p.size());
    }


    // 이미지 검색 조회 (10개)
    @Test
    void 이미지검색_조회_10() {
        Page<ProductImg> p = productImgSearchService.getProductImgListBySearchList("bocchi", 10, 0);
        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
    }

    // 이미지 검색 조회 (100개)
    @Test
    void 이미지검색_조회_100() {
        Page<ProductImg> p = productImgSearchService.getProductImgListBySearchList("bocchi", 100, 0);
        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
    }

    // 이미지 검색 조회 (1000개)
    @Test
    void 이미지검색_조회_1000() {
        Page<ProductImg> p = productImgSearchService.getProductImgListBySearchList("bocchi", 1000, 0);
        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
    }

    // 이미지 검색 조회 (전체)
    @Test
    void 이미지검색_조회_All() {
        Page<ProductImg> p = productImgSearchService.getProductImgListBySearchList("bocchi", 100000, 0);
        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
    }


    // 내 이미지 조회 (10개)
    @Test
    void 내_이미지_조회_10() {
        User user = userService.getUserById("");
        Page<ProductImg> p = productImgSearchService.getProductImgListByUserHotList(user, 10, 0);
        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());

        for (ProductImg productImg : p) {

        }
    }

    // 내 이미지 조회 (100개)
    @Test
    void 내_이미지_조회_100() {
        User user = userService.getUserById("");
        Page<ProductImg> p = productImgSearchService.getProductImgListByUserHotList(user, 100, 0);
        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
    }

    // 내 이미지 조회 (1000개)
    @Test
    void 내_이미지_조회_1000() {
        User user = userService.getUserById("");
        Page<ProductImg> p = productImgSearchService.getProductImgListByUserHotList(user, 1000, 0);
        System.out.println("TEST :: pageInfo :: totalElement = " + p.getTotalElements());
        System.out.println("TEST :: pageInfo :: totalPage = " + p.getTotalPages());
        System.out.println("TEST :: pageInfo :: hasNext = " + p.hasNext());
        System.out.println("TEST :: pageInfo :: NumberOfElement = " + p.getNumberOfElements());
    }


    // 이미지 1개 추가
    @Test
    @Rollback(false)
    void 이미지_추가() {
        Product product = productRepository.findById(1L).orElseThrow();
        User user = userRepository.findById("").orElseThrow();
        productImgService.upsertProductImg(ProductImg.createProductImg(product, ProductImgType.JPG, "testImg_" + 0, "test", user.getUserId()));

    }


    // 평가 1개 추가
    @Test
    @Rollback(false)
    void 평가_추가() {
        User user = userRepository.findById("").orElseThrow();
//        List<ProductImg> img = productImgRepository.findAllByProductImgIdIsGreaterThan(1000002L);
        ProductImg img = productImgRepository.findById(1000006L).orElseThrow();
        productImgScoreRepository.save(
                ProductImgScore.builder()
                        .userId(user.getUserId())
                        .productImgId(img.getProductImgId())
                        .score(109)
                        .build());
    }


    // 사용자 삭제 ( 및 사용자 관련 모든 데이터 삭제 )


}
