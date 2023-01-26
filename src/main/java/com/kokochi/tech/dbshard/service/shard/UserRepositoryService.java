package com.kokochi.tech.dbshard.service.shard;

import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import com.kokochi.tech.dbshard.shard.annotation.Sharding;
import com.kokochi.tech.dbshard.shard.enumType.ShardingTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
//@CacheConfig(cacheManager = "userCacheManager")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW, readOnly = true)
@Sharding(target = ShardingTarget.USER)
@RequiredArgsConstructor
public class UserRepositoryService implements ApplicationContextAware {

    private final UserRepository userRepository;


    @Transactional
    public void save(Long userId, User user) {
        userRepository.save(user);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("TEST :: UserRepositoryService - setApplicationContext() :: 1");
    }
}
