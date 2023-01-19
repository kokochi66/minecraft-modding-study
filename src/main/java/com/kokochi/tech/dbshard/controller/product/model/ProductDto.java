package com.kokochi.tech.dbshard.controller.product.model;

import com.kokochi.tech.dbshard.domain.product.Product;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductSeasonType;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDto {
    private Long productId;
    private ProductType productType;
    private ProductSeasonType productSeasonType;
    private String productTitle;
    private String productDirector;

    public Product convertProduct() {
        return Product.builder()
                .productType(productType)
                .productSeasonType(productSeasonType)
                .productTitle(productTitle)
                .productDirector(productDirector)
                .build();
    }

    public static ProductDto convertDto(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId())
                .productType(product.getProductType())
                .productSeasonType(product.getProductSeasonType())
                .productTitle(product.getProductTitle())
                .productDirector(product.getProductDirector())
                .build();
    }

}
