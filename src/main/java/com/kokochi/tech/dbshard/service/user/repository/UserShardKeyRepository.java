package com.kokochi.tech.dbshard.service.user.repository;

import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.domain.user.UserShardKey;
import com.kokochi.tech.dbshard.shard.annotation.MasterRepository;
import com.kokochi.tech.dbshard.shard.annotation.ShardingRepository;
import com.kokochi.tech.dbshard.shard.enumType.ShardingTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@MasterRepository
public interface UserShardKeyRepository extends JpaRepository<UserShardKey, Long> {

    UserShardKey findByUserId(String userId);

}
