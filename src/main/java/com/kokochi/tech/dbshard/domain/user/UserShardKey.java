package com.kokochi.tech.dbshard.domain.user;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "dsm_user_shard_key")
public class UserShardKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userShardKeyId;

    private String userId;

}
