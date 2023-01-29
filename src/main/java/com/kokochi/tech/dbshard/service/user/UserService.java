package com.kokochi.tech.dbshard.service.user;

import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.domain.user.UserHistory;
import com.kokochi.tech.dbshard.domain.user.UserShardKey;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserHistoryRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserShardKeyRepository;
import com.kokochi.tech.dbshard.shard.annotation.ShardService;
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
        return userRepository.findById(id).orElse(null);
    }

    // 로그인 검증
    public boolean verifyUser(String id, String password) {
        User user = userRepository.findById(id).orElseThrow();
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
        userRepository.delete(user);
    }

    // 검색 기록 저장
    public UserHistory upsertUserHistory(UserHistory userHistory) {
        return userHistoryRepository.save(userHistory);
    }

    // 검색 기록 가져오기
    public List<UserHistory> getUserHistoryList(User user) {
        return userHistoryRepository.findAllByUser(user);
    }

    private String generateShardKey() {
        return RandomString.make(16);
    }
}
