package com.kokochi.tech.dbshard.domain.product;

import com.kokochi.tech.dbshard.domain.product.enumType.ProductSeasonType;
import com.kokochi.tech.dbshard.domain.product.enumType.ProductType;
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
@ToString(of = {"productId", "productType", "productSeasonType", "productTitle", "productDirector", "regDate"})
@Table(name = "ds_product")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductSeasonType productSeasonType;

    private String productTitle;

    private String productDirector;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime regDate;

    @OneToMany(mappedBy = "product")
    private List<ProductImg> productImgList = new ArrayList<>();

    public Product createProduct(ProductType productType, ProductSeasonType productSeasonType, String title, String director) {
        return Product.builder()
                .productType(productType)
                .productSeasonType(productSeasonType)
                .productTitle(title)
                .productDirector(director)
                .regDate(LocalDateTime.now())
                .build();
    }

}
