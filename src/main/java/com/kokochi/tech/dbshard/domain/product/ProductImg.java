package com.kokochi.tech.dbshard.domain.product;

import com.kokochi.tech.dbshard.domain.product.enumType.ProductImgType;
import com.kokochi.tech.dbshard.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "ds_product_img")
public class ProductImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productImgId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private ProductImgType productImgType;

    private String productImgTitle;

    private String productImgUrl;

    @CreatedDate
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User uploadUser;

}
