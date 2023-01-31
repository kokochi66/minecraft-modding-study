package com.kokochi.tech.dbshard.domain.user;

import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.shard.ShardKeyObject;
import com.kokochi.tech.dbshard.domain.user.enumType.UserHistoryType;
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
@Table(name = "dss_user_history", indexes = {
        @Index(name = "idx_user", columnList = "userId")
})
public class UserHistory implements ShardKeyObject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productImgId")
    private ProductImg productImg;

    @Enumerated(EnumType.STRING)
    private UserHistoryType userHistoryType;

    @CreatedDate
    private LocalDateTime regDate;

    @Override
    public String getUserId() {
        return user.getUserId();
    }
}
