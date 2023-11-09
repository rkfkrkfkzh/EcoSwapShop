# EcoSwapShop
***
## 소개

EcoSwapShop은 중고 물품 거래를 위한 웹사이트로, 사용자들이 효율적으로 물품을 사고팔 수 있는 플랫폼을 제공합니다. \
이 프로젝트는 지속 가능한 소비를 장려하고 환경에 미치는 영향을 줄이기 위해 시작되었습니다. \
Spring boot 를 사용하여 REST API를 설계 및 구축 Thymeleaf와 연동하여 중고거래 웹 사이트 제작했습니다
***
## 주요 기능

- 댓글 및 답글
- 카테고리 분류
- 메시지 알림설정
- 비밀번호 암호화
- 실시간 채팅 기능
- 사용자 계정 관리
- 상품에 관한 찜기능
- 검색 및 필터링 기능
- 게시글 등록 및 관리
- 상품 등록 및 관리 상세보기
- 비밀번호 분실시 이메일로 서비스
***
##  사용 기술

- Java 17
- Gradle
- Spring Data JPA
- P6Spy
- Spring Security
- SpringBoot 2.5.4
- Spring WepSockets
- Spring Boot Mail
- Lombok
- BCrypt
- SockJS
- HTML/CSS
- Thymeleaf
- JavaScript
- Ajax
- JUnit
- STOMP
- REST API
- Hibernate
- H2 Database
***
## Entity Relationship Diagram
![Entity Diagram](uploads/entity.png)
***
## Class Diagram
![Class Diagram](uploads/class.png)
***
## 구현
### 보안 및 인증
- Spring Security와 통합하여 CSRF 보호를 제공, 사용자 인증 및 인가
- CSRF Token을 사용한 인증 시스템

### 데이터 처리
- 각 Repository 인터페이스는 JpaRepository를 상속받아, CRUD 연산을 위한 기본적인 메서드를 자동으로 제공
- Spring Data JPA 와 H2 DB를 사용하여 데이터를 저장하고 처리
- Repository 사용하여 비즈니스 로직을 처리하는 Service 계층을 구현
- Pageable 인터페이스를 파라미터로 받는 메서드를 통해 페이징과 정렬 기능을 쉽게 구현
- UUID를 키로 사용하여 채팅 세션을 관리

### Thymeleaf로 프론트엔드 개발
- Bootstrap CSS 프레임워크를 사용하여 스타일링을 적용
- SockJS와 Stomp.js를 활용해 백엔드 서버와의 웹소켓 연결을 설정.(실시간 채팅)
- Spring Security와의 통합을 통해 CSRF 토큰 관리와 같은 보안 기능을 손쉽게 구현
- $.ajax 함수를 사용하여 서버에 비동기적으로 HTTP 요청, 사용자의 입력에 대한 피드백을 제공
- 메타 태그 viewport를 사용하여 모바일 및 다양한 화면 크기에 대응하는 반응형 웹 디자인을 적용

### CI/CD 구축

- Github Repository 로 관리
- GitHub Actions를 사용하여 Java 애플리케이션에 대한 CI/CD 파이프라인을 구축

### 배포 설정

- GitHub Action을 사용하여 AWS Elastic Beanstalk에 애플리케이션을 배포

# 6. 설명

## [1] 회원가입

## [2] 메인 게시판
