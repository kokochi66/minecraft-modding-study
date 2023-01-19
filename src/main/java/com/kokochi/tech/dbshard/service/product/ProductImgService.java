package com.kokochi.tech.dbshard.service.product;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductImgType;
import com.kokochi.tech.dbshard.service.file.FileService;
import com.kokochi.tech.dbshard.service.product.repository.ProductImgRepository;
import com.kokochi.tech.dbshard.service.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductImgService {

    private final ProductImgRepository productImgRepository;
    private final FileService fileService;

    @Transactional
    public Long insertProductImg(ProductImg productImg, MultipartFile file) {
        String url = fileService.save(file);
        productImg.setProductImgType(ProductImgType.GIF);
        productImg.setProductImgUrl(url);
        productImg.setRegDate(LocalDateTime.now());
        System.out.println("TEST :: productImg = " + productImg);
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


}
