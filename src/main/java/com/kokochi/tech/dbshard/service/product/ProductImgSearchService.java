package com.kokochi.tech.dbshard.service.product;

import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.shard.ShardNo;
import com.kokochi.tech.dbshard.domain.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductImgSearchService {

    private final ProductImgService productImgService;
    private final ProductService productService;

    // 인기 이미지 조회
    public Page<ProductImg> getProductImgListByHotList(int size, int offset) {


        Page<ProductImg> page1 = productImgService.getProductImgListByHotList(new ShardNo(0), size, offset);
        for (ProductImg productImg : page1) {
            System.out.println("TEST :: page1 = " + productImg);
        }

        Page<ProductImg> page2 = productImgService.getProductImgListByHotList(new ShardNo(1), size, offset);
        for (ProductImg productImg : page2) {
            System.out.println("TEST :: page2 = " + productImg);
        }
        List<ProductImg> pageList = page1.getContent();
        pageList.addAll(page2.getContent());
        return page1;
    }

    // 검색 조회 (이름순)
    public Page<ProductImg> getProductImgListBySearchList(String searchCondition, int size, int offset) {
//        PageRequest pageRequest = PageRequest.of(offset, size);
//        return productImgRepository.findByProductImgTitleContains(searchCondition, pageRequest);
        return null;
    }

    // 특정 사용자의 이미지 조회
    public Page<ProductImg> getProductImgListByUserHotList(User user, int size, int offset) {
//        PageRequest pageRequest = PageRequest.of(offset, size);
//        return productImgRepository.findByUploadUserHotListPaging(user, pageRequest);
        return null;
    }

}
