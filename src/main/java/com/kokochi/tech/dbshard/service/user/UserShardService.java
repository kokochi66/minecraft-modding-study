package com.kokochi.tech.dbshard.service.user;

import com.kokochi.tech.dbshard.domain.shard.ShardNo;
import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.user.repository.UserHistoryRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserShardKeyRepository;
import com.kokochi.tech.dbshard.shard.annotation.ShardService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ShardService
@AllArgsConstructor
public class UserShardService {

    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final UserShardKeyRepository userShardKeyRepository;

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    protected List<User> getShardUser(ShardNo shardNo) {
        return userRepository.findAll();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User saveUser(User user)  {
        return userRepository.save(user);
    }
}
