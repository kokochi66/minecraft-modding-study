package com.kokochi.tech.dbshard.service.user;

import com.kokochi.tech.dbshard.domain.shard.ShardNo;
import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.domain.user.UserShardKey;
import com.kokochi.tech.dbshard.service.user.repository.UserHistoryRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserShardKeyRepository;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class UserService {

    private final UserShardService userShardService;
    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final UserShardKeyRepository userShardKeyRepository;

    // 사용자 찾기
    public User getUserById(String id) {
        return userShardService.getUserById(id);
    }

    // 로그인 검증
    public boolean verifyUser(String id, String password) {
        User user = getUserById(id);
        return password.equalsIgnoreCase(user.getPassword());
    }

    // 사용자 저장 (회원가입)
    @Transactional
    public User upsertUser(User user) {
        String shardKey = generateShardKey();
        userShardKeyRepository.save(UserShardKey.builder().userId(shardKey).build());
        user.setUserId(shardKey);
        return userShardService.saveUser(user);
    }

    // 회원 제거
    public void deleteUser(User user) {
        userShardService.deleteUser(user);
    }

    private String generateShardKey() {
        return RandomString.make(16);
    }

    // 모든 샤드의 사용자 데이터 가져오기
    public List<User> getAllUserShard() {
        List<User> shard1 = userShardService.getShardUser(new ShardNo(0));
        List<User> shard2 = userShardService.getShardUser(new ShardNo(1));
        shard1.addAll(shard2);
        return shard1;
    }
}
