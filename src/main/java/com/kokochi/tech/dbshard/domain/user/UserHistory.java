package com.kokochi.tech.dbshard.domain.user;

import com.kokochi.tech.dbshard.domain.product.ProductImg;
import com.kokochi.tech.dbshard.domain.product.ProductImgScore;
import com.kokochi.tech.dbshard.domain.user.enumType.UserHistoryType;
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
@Table(name = "ds_user_history", indexes = {
        @Index(name = "idx_user", columnList = "userId")
})
public class UserHistory {

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
}
