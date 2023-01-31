package com.kokochi.tech.dbshard.service.product;

import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.product.ProductImgScore;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductImgType;
import com.kokochi.tech.dbshard.domain.shard.ShardNo;
import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.file.FileService;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgScoreRepository;
import com.kokochi.tech.dbshard.shard.annotation.ShardService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@ShardService
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

    public List<ProductImg> getProductImgByUserId(User user) {
        return productImgRepository.findAllByUploadUser(user);
    }

    // 이미지 평가 수정
    public ProductImgScore upsertProductImgScore(ProductImgScore productImgScore) {
        return productImgScoreRepository.save(productImgScore);
    }

    // 인기 이미지 조회
    protected Page<ProductImg> getProductImgListByHotList(ShardNo shardNo, int size, int offset) {
        PageRequest pageRequest = PageRequest.of(offset, size);
        return productImgRepository.findByHotListPaging(pageRequest);
    }

    // 검색 조회 (이름순)
    protected Page<ProductImg> getProductImgListBySearchList(ShardNo shardNo, String searchCondition, int size, int offset) {
        PageRequest pageRequest = PageRequest.of(offset, size);
        return productImgRepository.findByProductImgTitleContains(searchCondition, pageRequest);
    }

    // 특정 사용자의 이미지 조회
    protected Page<ProductImg> getProductImgListByUserHotList(ShardNo shardNo, User user, int size, int offset) {
        PageRequest pageRequest = PageRequest.of(offset, size);
        return productImgRepository.findByUploadUserHotListPaging(user, pageRequest);
    }

}
