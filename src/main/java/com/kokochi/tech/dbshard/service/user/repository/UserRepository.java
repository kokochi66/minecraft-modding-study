package com.kokochi.tech.dbshard.service.user.repository;

import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.shard.annotation.ShardingRepository;
import com.kokochi.tech.dbshard.shard.enumType.ShardingTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ShardingRepository(target = ShardingTarget.USER)
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

}
