# 프로젝트 기획 - 짤방 저장소 프로젝트

- 작품 카테고리가 존재, 작품 카테고리 하위에 짤방이 생겨남
  - 작품은 계속해서 늘어나는 데이터이므로, Hash Sharding으로 관리
- 짤방을 찾으려 하는 사용자
  - 사용자의 짤방을 찾은 이력, 좋아하는 짤방 등 데이터를 저장
  - 사용자 수는 Dynamic Sharding으로 관리

## 테스트 요소

- 작품 카테고리를 Hash Sharding으로 관리하더라도, 특정 인기 작품의 하위 짤방만 계속 쌓이면, 특정 해시에만 데이터가 많이 쌓일 수 있음 (이 경우 얼마만큼 성능 이슈가 발생하는지 테스트)

## 상세 기획 및 작품구조 기획

- Product
  - productId
  - productType
  - productTitle
  - productReleaseDateType
  - productDirector
- ProductImg
  - productImgId
  - productId
  - productImgType
  - productImgUrl
  - productImgTitle
  - regDate
  - uploadUser
- User
  - userId
  - userName
  - regDate
- UserViewHistory
  - userId
  - historyDate

## 구현 사항

- [ ] 로그인 기능 구현
- [ ] Junit 테스트 형식 구현
- [ ] 작품 CRUD 구현
- [ ] 클라이언트 간단 CRUD 구현

## 클라에서 필요한 페이지

- 메인 페이지
  - 타이틀, 검색기능, 인기 이미지 등
- 검색 페이지
  - 검색 시 이동 되는 페이지, 이미지 리스트, ㅠ작품 리스트를 표시
  - 검색 필터 기능, 검색 카테고리 기능 추가
- 작품 추가/수정 페이지
- 작품 상세 페이지
  - 댓글 기능? 좋아요 등
- 이미지 추가/수정 페이지
- 이미지 상세 페이지
- 로그인 페이지
- 회원가입 페이지
- 회원정보 수정 페이지
- 내가 좋아요 한 이미지 페이지