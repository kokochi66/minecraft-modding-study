package com.kokochi.tech.dbshard.domain.user;

import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.product.ProductImgScore;
import com.kokochi.tech.dbshard.domain.shard.ShardKeyObject;
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
@ToString(of = {"userId", "userName"})
@Table(name = "dss_user")
public class User implements ShardKeyObject {

    @Id
    private String userId;

    private String userName;
    private String password;

    @CreatedDate
    private LocalDateTime regDate;

    @Transient
    private List<ProductImgScore> productImgScoreList = new ArrayList<>();


    public static User createUser(String userName, String password) {
        return User.builder()
                .userName(userName)
                .password(password)
                .regDate(LocalDateTime.now())
                .build();
    }
}
