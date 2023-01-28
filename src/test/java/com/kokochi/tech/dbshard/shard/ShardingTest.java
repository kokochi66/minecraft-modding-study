package com.kokochi.tech.dbshard.shard;

import com.kokochi.tech.dbshard.domain.user.User;
import com.kokochi.tech.dbshard.shard.property.TestProperty;
import com.kokochi.tech.dbshard.service.shard.UserRepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShardingTest {

    @Autowired private UserRepositoryService userRepositoryService;
    @Autowired private TestProperty testProperty;

    @Test
    void 테스트() {
        User testUser = User.createUser("testUser", "1234");
        userRepositoryService.save(10L, testUser);
//        System.out.println("TEST :: user is null = " + (user == null));
//        if (user != null) {
//            for (ShardingDataSourceProperty.Shard shard : user.getShards()) {
//                System.out.println("TEST :: username = " + shard.getUsername());
//                System.out.println("TEST :: password = " + shard.getPassword());
//
//                System.out.println("TEST :: masterName = " + shard.getMaster().getName());
//                System.out.println("TEST :: masterUrl = " + shard.getMaster().getUrl());
//
//
//                System.out.println("TEST :: slaves ============================");
//                for (ShardingDataSourceProperty.Property slave : shard.getSlaves()) {
//                    System.out.println("TEST :: slaveName = " + slave.getName());
//                    System.out.println("TEST :: slaveUrl = " + slave.getUrl());
//                }
//            }
//        }

//        UserHolder.setSharding(ShardingTarget.USER, 0L);

        System.out.println("TEST :: confirm");

    }

}
