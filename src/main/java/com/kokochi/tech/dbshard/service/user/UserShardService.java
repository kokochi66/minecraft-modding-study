package com.kokochi.tech.dbshard.service.user;

import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.user.repository.UserHistoryRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserShardKeyRepository;
import com.kokochi.tech.dbshard.shard.annotation.ShardService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ShardService
@AllArgsConstructor
public class UserShardService {

    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final UserShardKeyRepository userShardKeyRepository;

    @Transactional
    public User saveUser(User user)  {
        return userRepository.save(user);
    }
}
