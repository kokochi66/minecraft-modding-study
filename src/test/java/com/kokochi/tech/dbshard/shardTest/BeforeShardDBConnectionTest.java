package com.kokochi.tech.dbshard.shardTest;

import com.kokochi.tech.dbshard.scenario.AbstractScenarioTest;
import com.kokochi.tech.dbshard.service.user.UserService;
import com.kokochi.tech.dbshard.service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@Transactional
public class BeforeShardDBConnectionTest extends AbstractScenarioTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;


    // 인기 이미지 조회 (10개)

    // 인기 이미지 조회 (50개)

    // 인기 이미지 조회 (1000개)

    // 인기 이미지 조회 (전체)




    // 이미지 검색 조회 (10개)

    // 이미지 검색 조회 (100개)

    // 이미지 검색 조회 (1000개)

    // 이미지 검색 조회 (전체)



    // 내 이미지 조회 (10개)

    // 내 이미지 조회 (100개)

    // 내 이미지 조회 (전체)


    // 내가 좋아하는 이미지 조회 (10개)

    // 내가 좋아하는 이미지 조회 (100개)

    // 내가 좋아하는 이미지 조회 (전체)




    // 이미지 1개 추가

    // 이미지 10개 추가

    // 이미지 100개 추가



    // 평가 1개 추가

    // 평가 10개 추가

    // 평가 100개 추가



    // 이미지 수정

    // 평가 수정


    // 사용자 삭제 ( 및 사용자 관련 모든 데이터 삭제 )





}
