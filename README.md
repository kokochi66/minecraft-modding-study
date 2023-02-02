# DB sharding 스터디 프로젝트

- DB Sharding을 스터디 하기 위한 기술과제 겸 실습 개인 프로젝트 입니다.
- [DB 샤딩 스터디 정리 문서](./Study.md)
- [프로젝트 기획](./Project.md)

## 기술스택
- 스프링 부트 2.7.7
- MYSQL
- Spring Data JPA
- Thymeleaf

## 참고
- https://techblog.woowahan.com/2687
- https://sophia2730.tistory.com/entry/Databases-Database-Sharding%EC%83%A4%EB%94%A9
- https://nesoy.github.io/articles/2018-05/Database-Shard
- https://sg-choi.tistory.com/570
- https://supawer0728.github.io/2018/05/06/spring-boot-multiple-datasource/

구현 방식에 대해서는 따로 스터디 후 정리할 예정

## 진행계획 및 Manday 설정
- [x] DB 샤딩 스터디 및 구현방향 설정 2MD
- [x] 클라이언트 구현 5MD
- [x] 서버 기본 구조 구현(샤딩 되지 않은) 10MD
    - [x] 기본 데이터 CRUD
    - [x] 검색 기능 구현
        - [x] 카테고리 별 검색 (작품 별 검색, 시즌 별 검색, 타입별 검색)
        - [x] 검색 필터링
- [x] DB 샤딩 구현 10MD
- [x] 각종 테스트 및 스터디 결과 포스팅 15MD



## 스터디 프로젝트 마무리

- 샤딩을 적용한 DB의 실제 성능 테스트까지 하여, 성능이 얼마나 좋아졌는지를 비교해보고 싶었으나, 기본적인 설계가 잘못되었음
  - 샤드 To 샤드 간 쿼리를 하는 것 까지는 좋았으나, 샤드 간 페이징을 거는 부분에서 상당히 설계가 잘못되었다는 부분을 깨달음 ... (결국 Spring Data JPA를 사용할 수 없게 되었고, jdbcTemplate을 사용해야만 했음)
  - JdbcTemplate을 사용하면 샤드 To 샤드 의 페이징까지 가능하나, 결국 이렇게까지 복잡한 구조를 갖게된다면 스터디 프로젝트를 하는 이유가 없어졌다.
- 그래도 샤딩을 적용하기 위해서는 어떤 설정들이 기본적으로 필요하며, 어떤 부분들을 유의해야하고, 설계를 어떤식으로 가져가야 할지를 배워볼 수 있었음
  - 기본적으로 샤딩을 적용하는 순간 프로젝트가 굉장히 복잡해지고, 여러가지 규칙들을 적용해야되므로, 그냥 샤딩은 적용하지 않을 수 있다면 적용하지 않는게 가장 베스트로 보임
- 앞으로 무언가 샤딩이 필요하다면 해당 프로젝트를 참고할 수 있을 듯