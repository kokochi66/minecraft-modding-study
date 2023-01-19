package com.kokochi.tech.dbshard.domain.product;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImgScorePK implements Serializable {
    private Long productImgId;
    private Long userId;

}
