package com.kokochi.tech.dbshard.service.product;

import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.product.ProductImgScore;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductImgType;
import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.file.FileService;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ProductImgService {

    private final ProductImgRepository productImgRepository;
    private final ProductImgScoreRepository productImgScoreRepository;
    private final FileService fileService;

    @Transactional
    public Long insertProductImg(ProductImg productImg, MultipartFile file) {
        if (file != null) {
            String url = fileService.save(file);
            productImg.setProductImgUrl(url);
        }

        productImg.setProductImgType(ProductImgType.GIF);
        productImg.setRegDate(LocalDateTime.now());
        Long savedImageId = upsertProductImg(productImg);
        return savedImageId;
    }


    @Transactional
    public Long upsertProductImg(ProductImg productImg) {
        ProductImg savedProductImg = productImgRepository.save(productImg);
        return savedProductImg.getProductImgId();
    }

    public ProductImg getProductImgById(Long id) {
        return productImgRepository.findById(id).orElse(null);
    }


    // 인기 이미지 조회
    public Page<ProductImg> getProductImgListByHotList(int size, int offset) {
        PageRequest pageRequest = PageRequest.of(offset, size);
        return productImgRepository.findByHotListPaging(pageRequest);
    }

    // 검색 조회 (이름순)
    public Page<ProductImg> getProductImgListBySearchList(String searchCondition, int size, int offset) {
        PageRequest pageRequest = PageRequest.of(offset, size);
        return productImgRepository.findByProductImgTitleContains(searchCondition, pageRequest);
    }

    // 특정 사용자의 이미지 조회
    public Page<ProductImg> getProductImgListByUserHotList(User user, int size, int offset) {
        PageRequest pageRequest = PageRequest.of(offset, size);
        return productImgRepository.findByUploadUserHotListPaging(user, pageRequest);
    }

    // 이미지 평가 수정
    public ProductImgScore upsertProductImgScore(ProductImgScore productImgScore) {
        return productImgScoreRepository.save(productImgScore);
    }



}
