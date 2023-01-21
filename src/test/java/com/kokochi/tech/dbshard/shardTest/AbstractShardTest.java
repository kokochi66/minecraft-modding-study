package com.kokochi.tech.dbshard.shardTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class AbstractShardTest {

    protected Long timesecs;

    @BeforeEach
    void test_before() {
        timesecs = System.currentTimeMillis();
    }

    @AfterEach
    void test_after() {
        Long after = System.currentTimeMillis();
        System.out.println("TEST :: testSecs = " + (after - timesecs));
    }
}
