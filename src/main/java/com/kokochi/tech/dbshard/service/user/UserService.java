package com.kokochi.tech.dbshard.service.user;

import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.domain.user.UserHistory;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import com.kokochi.tech.dbshard.service.user.repository.UserHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;

    // 사용자 찾기
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 로그인 검증
    public boolean verifyUser(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow();
        return password.equalsIgnoreCase(user.getPassword());
    }

    // 사용자 저장 (회원가입)
    public User upsertUser(User user) {
        return userRepository.save(user);
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
}
