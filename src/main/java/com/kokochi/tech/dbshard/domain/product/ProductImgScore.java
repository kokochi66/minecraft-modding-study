package com.kokochi.tech.dbshard.domain.product;

import com.kokochi.tech.dbshard.domain.user.User;
import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@IdClass(ProductImgScorePK.class)
@Table(name = "ds_product_img_score")
public class ProductImgScore {
    @Id
    private Long productImgId;
    @Id
    private Long userId;
    private int score;
}
