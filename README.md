<h2 align=center>해외축구 통합 커뮤니티 프로젝트(PL)📚</h2>

<p align=center> 📆 마이크로디그리 자바 풀스택 과정 2024.10.1 ~ 2024.12.13</p>
<p align=center> 📆 보완 및 개선점 적용 2025.01.08 ~ 2025.01.26</p>

<div align=center>
 <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
 <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white">
 <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white">
 <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white">
 <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">
 <img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white">
 <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">
 <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white">
 <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
 <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>
<br>
<p align=center><img src=./src/assets/images/velogClone.gif  width=60% /></p>
<p align=center> 🏠 <a href="http://3.107.177.97:8080/matches/list">웹 페이지</a></p>
<p align=center> 🏠 <a href="https://www.figma.com/design/b2DXHp0z1MRs2ynEHhDaPr/Untitled?node-id=0-1&t=6RJAY6g1EFZIBIrV-1">초기 화면정의 (피그마)</a></p>
<p align=center> 💼 <a href=https://github.com/jangmireu/memorial/wiki/%ED%99%94%EB%A9%B4-%EA%B5%AC%EC%84%B1-%EB%B0%8F-%EC%A3%BC%EC%9A%94%EA%B8%B0%EB%8A%A5-%EC%84%A4%EB%AA%85> 화면 구성 및 주요 기능 설명</a></p>

## 1. 프로젝트 살펴보기 🔎
### 🙎‍♂️ 팀 구성 
|장미르|박진우|
| :---: | :---: |
|Full-stack|Full-stack|

- 풀스택 개발자 2명

<hr/>

### 🔥 개요

4학년 2학기 마이크로디그리 자바 풀스택 과정에서 만들었던 PL프로젝트를 보완하고 발전시키고자 프로젝트를 진행함

### 👨‍💻 주요 기능

- 경기 일정 확인 및 실시간 경기 결과 제공
- 실시간 채팅방 기능
- 커뮤니티 게시판 및 댓글 관리
- 포인트 배팅 및 결과 확인
- 포인트로 상점 아이템 구매

### 💻 기술 스택

- Java Spring Boot: REST API, WebSocket
- Thymeleaf: 동적 HTML 템플릿 엔진
- MySQL: 관계형 데이터베이스
- Hibernate: ORM 관리
- Amazon EC2: 클라우드 배포 환경
- HTML5, CSS3, JavaScript
<br>


> #### 📁 프로젝트 구조

```
src/
├─ main/
│  ├─ java/com/example/football/
│  │  ├─ controller/ (REST 컨트롤러)
│  │  ├─ dto/ (데이터 전송 객체)
│  │  ├─ entity/ (데이터베이스 엔티티)
│  │  ├─ repository/ (JPA 리포지토리)
│  │  ├─ service/ (비즈니스 로직)
│  ├─ resources/
│     ├─ static/ (정적 자원: CSS, JS, 이미지)
│     ├─ templates/ (HTML 템플릿)
├─ test/
│  ├─ (테스트 케이스)
target/
├─ classes/ (빌드된 클래스)
```

<br>
<hr/>

# 2. 협업 👤

### 🪚 협업을 위한 툴

- Discord
  - 개발 진행 상황 공유하기 위해 사용했습니다.
- Git, Github
  - 코드 버전 관리 및 공유하기 위해 사용했습니다.
<hr/>

# 3. 배포 👨‍🔧

#### 배포 환경

1. Amazon EC2: 애플리케이션 배포
2. 자동 빌드 및 테스트: Maven 활용
3. 엔드포인트: http://3.107.177.97:8080


> #### 📜 배포 과정 설명

1. main 브랜치에서 코드 병합 후 EC2로 배포
2. MySQL DB 연결 및 Spring Boot 애플리케이션 실행
3. 엔드 유저는 EC2의 IP로 웹 서비스 접속 가능
