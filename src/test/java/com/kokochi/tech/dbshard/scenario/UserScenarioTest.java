package com.kokochi.tech.dbshard.scenario;

import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.service.user.UserService;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
public class UserScenarioTest extends AbstractScenarioTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    // 회원 가입
    @Test
    void 회원_가입() {
        User user = userService.upsertUser(User.createUser("testUser1", "111"));

        User findUser = userService.getUserById(user.getUserId());
        assertThat(findUser.getUserName()).isEqualTo(user.getUserName());
    }

    // 사용자 삭제
    @Test
    @Rollback(false)
    void 회원_삭제() {
        User user = userService.upsertUser(User.createUser("testUser1", "111"));
        userService.deleteUser(user);

        User findUser = userService.getUserById(user.getUserId());
        assertThat(findUser).isNull();
    }

    // 회원 다량 추가
    @Test
    @Rollback(false)
    void 회원_다량추가() {
        List<User> userList = new ArrayList<>();
        for (int i=0;i<100;i++) {
            userList.add(User.createUser("testUser_"+i, "111"));
        }
        userRepository.saveAll(userList);
    }
}
