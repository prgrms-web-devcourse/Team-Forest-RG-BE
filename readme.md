# RG(Riding is Good) - Server
## 📝 프로젝트 소개
![RG_Logo5](https://user-images.githubusercontent.com/72663337/184543063-36de836d-b3a4-4772-b846-139d13a52677.png)

<div align='center'>
  <h3> RG(Riding is Good)은 자전거를 즐기는 사람들을 위해, 
    빠르고 편리하게 자전거 모임을 주최, 참여할 수 있는 서비스입니다. </h3>
  <p>기존 자전거 커뮤니티 서비스들의 노후화, 편의성의 부재로 인한 불편함을 해소하기 위한 서비스입니다.</p>
  <p> 또한 적절한 매칭을 위해, 평가 서비스를 통하여 노쇼 유저를 제한하는 기능 또한 제공합니다.</p>
</div>
  
<br>

## 🔗 프로젝트 관련 링크
> 💡 [배포 사이트](https://cool-dusk-ced14a.netlify.app/)
- [프로젝트 노션 페이지](https://backend-devcourse.notion.site/05-Forest-2164c428cb744e27befa34a395de1769)
- [프론트엔드 Repo](https://github.com/prgrms-web-devcourse/Team-Forest-RG-FE)

<br>

## 🧑‍💻 팀원 소개

<div align='center'>
<table>
  <tr>
    <td align="center"><b>Scrum Master</b></td>
    <td align="center"><b>Lead Developer</b></td>
    <td align="center"><b>Developer</b></td>
    <td align="center"><b>Developer</b></td>
    <td align="center"><b>Developer</b></td>
  </tr>
  <tr>
    <td>
        <a href="https://github.com/HunkiKim">
            <img src="https://avatars.githubusercontent.com/u/66348135?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/epicblues">
            <img src="https://avatars.githubusercontent.com/u/19306609?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/kkj0419">
            <img src="https://avatars.githubusercontent.com/u/72663337?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/blessing333">
            <img src="https://avatars.githubusercontent.com/u/65841596?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/res-cogitans">
            <img src="https://avatars.githubusercontent.com/u/54278885?v=4" width="100px" />
        </a>
    </td>
  </tr>

  <tr> 
    <td align="center"><a href="https://github.com/HunkiKim">Partey(김훈기)</a></td>
    <td align="center"><a href="https://github.com/epicblues">Bob(김민성)</a></td>
    <td align="center"><a href="https://github.com/kkj0419">Didi(김현정)</a></td>
    <td align="center"><a href="https://github.com/blessing333">Pray(이민재)</a></td>
    <td align="center"><a href="https://github.com/res-cogitans">Kant(이한빈)</a></td>
  </tr>

</table>
</div>

- **Partey** : User 회원 가입 및 Kakao OAuth 연동 
- **Bob** : 백엔드 CI/CD 파이프라인 구축 및 관리, Riding 댓글 API 구현, 배포 환경 이슈 트러블 슈팅
- **Didi** : Riding 생성 및 수정, 평가 기능 API
- **Pray** : 라이딩 조회, 라이딩 조건별 검색, 알림 서비스
- **Kant** : ERD 설계, 사용자 정보 수정 및 조회 기능 개발, 이미지 업로드 기능 개발

<br>

## ⚒ 기술스택

### Spring

<img src="https://img.shields.io/badge/JAVA 11-007396?style=for-the-badge&logo=Java&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Rest Docs-6DB33F?style=for-the-badge"/></a>
<img src="https://img.shields.io/badge/QueryDSL-gray?style=for-the-badge&logoColor=white"/></a>

### Build

<img src="https://img.shields.io/badge/Gradle-4429A1?style=for-the-badge&logo=Gradle&logoColor=02303A"/></a>

### Database

<img src="https://img.shields.io/badge/MySQL 8-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/></a>

### DevOps

<img src="https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white"/></a>
<img src="https://img.shields.io/badge/RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white"/></a>
<img src="https://img.shields.io/badge/S3-527FFF?style=for-the-badge&logo=Amazon S3&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Github Action-2088FF?style=for-the-badge&logo=Amazon RDS&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Amazon RDS&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white"/></a>

### Test

<img src="https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"/></a>
<img src="https://img.shields.io/badge/nGrinder-03C75A?style=for-the-badge&logo=Naver&logoColor=white"/></a>

<br>

## 💌 기능 소개

<details>
    <summary><b>카카오 회원가입/로그인</b></summary>
  
- 카카오 간편 회원가입을 통하여 서비스에 가입하고, 로그인할 수 있음
</details>

<br>

<details>
    <summary><b>라이딩 참여</b></summary>
  
- 모집 중인 라이딩에 참여 신청을 할 수 있음
</details>

<br>

<details>
    <summary><b>라이딩 주최</b></summary>
  
- 지정된 양식에 따라 라이딩 모집 게시글을 편리하게 작성할 수 있음
</details>

<br>

<details>
    <summary><b>라이딩 검색</b></summary>

- 자전거 종류, 모집 상태, 레벨, 지역에 따른 라이딩 모집 게시글을 검색할 수 있음
</details>

<br>

<details>
    <summary><b>알림</b></summary>
  
- 주최했던 라이딩에 대하여, 다른 사용자가 신청-취소하면 그에 대한 알림을 받을 수 있음
</details>

<br>

<details>
    <summary><b>마이페이지</b></summary>
  
- 참여한, 참여했던 라이딩 정보와, 내가 주최했던 라이딩 게시글 정보들을 볼 수 있음
- 회원 정보를 수정할 수 있음
</details>

<br>

<details>
    <summary><b>평가</b></summary>
  
- 참여했던 라이딩에 대하여, 참여자들을 평가할 수 있음
- 라이딩을 주최한 라이딩 리더는, 멤버들의 노쇼 여부를 체크하여 해당 멤버에게 제약을 줄 수 있음
</details>

<br>


## 💬 아키텍처
### 배포 아키텍처
![deploy drawio](https://user-images.githubusercontent.com/72663337/184635974-7d1f9e9c-6133-4212-aaac-3e5a95e9779f.png)
- **Github Actions**를 통한 CI/CD 를 진행하고, 이를 통과해야만 PR이 merge됩니다.
- build된 **Docker image**를 **dockerhub**으로 push하고, **EC2** 서버의 **docker**에서 이를 pull받아 실행하는 방식으로 배포를 진행합니다.

### 서비스 아키텍처
![Untitled Diagram drawio (1)](https://user-images.githubusercontent.com/72663337/184794418-78f46f4a-1b41-4114-ab26-593d72ced230.png)

- 서버에서 발생한 에러에 대한 메시지를 **slack, file**으로 전달하여 확인할 수 있습니다.
- **nGrinder**, **PINPOINT**를 사용하여 부하 테스트를 진행, 서버의 요청 처리 성능을 모니터링합니다.

<br>

## 🙌 협업 방식

### Git 컨벤션

- 브랜치 전략 : Git-Flow 사용
  - `main` : EC2서버로 배포되는 브랜치
  - `develop` : 배포되기 위한 타깃 브랜치
  - `feature` : 기능 개발 브랜치
  - `hotfix` : main에서 발생한 버그를 해결하기 위한 브랜치
  - `release` : 추후 추가된 브랜치로, 배포 전용 브랜치 

### 협업 툴

- `Zenhub` : 개발 및 이슈 관리를 위한 협업 툴로 사용
- `Slack` : 비동기적 소통 툴로 사용
- `Notion` : 주요한 문서 작업(일정 관리, 엔지니어링 위키 등)을 위한 툴로 사용

### 헥사고날 아키텍처

- `Port & Adapter` 구조를 이용하여, 도메인 로직이 infra계층에 의존하는 것을 최소화하는 걸 목표로 적용하였습니다.

<br>

## 📊 API 명세서


## 🖌 ERD

![forest-rg](https://user-images.githubusercontent.com/72663337/184542888-b0c0c238-5b6a-41bd-8c41-6e4015be2da7.png)
