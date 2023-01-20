package com.kokochi.tech.dbshard.domain.product;

import com.kokochi.tech.dbshard.domain.product.enumType.ProductImgType;
import com.kokochi.tech.dbshard.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(of = {"productImgId", "productImgType", "productImgTitle", "productImgUrl"})
@Table(name = "ds_product_img")
public class ProductImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productImgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    @Enumerated(EnumType.STRING)
    private ProductImgType productImgType;

    private String productImgTitle;

    private String productImgUrl;

    @CreatedDate
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User uploadUser;

    @Transient
    private List<ProductImgScore> productImgScoreList = new ArrayList<>();

    @Transient
    private Double scoreAvg;

    public static ProductImg createProductImg(Product product, ProductImgType productImgType, String productImgTitle, String productImgUrl, User uploadUser) {
        return ProductImg.builder()
                .product(product)
                .productImgType(productImgType)
                .productImgTitle(productImgTitle)
                .productImgUrl(productImgUrl)
                .regDate(LocalDateTime.now())
                .uploadUser(uploadUser)
                .build();
    }
}
