# 바다환경지킴이 사업을 위한 통합 플랫폼
<br>
<p align="center"><img src="https://github.com/user-attachments/assets/231ac93d-c6ba-43fc-a188-0c9732c40747" width="200"></p>
<br>

## 👩‍👧‍👦 기획 배경
'바다환경지킴이 사업'은 **전국 해안을 접한 기초 지자체마다 상시 전담관리 인력인 바다환경지킴이를 배치하여 해안가를 순회하며 쓰레기를 수거하는 사업**입니다. (24년도 기준) 지금까지의 청소 프로세스는 단순 수거 목적에 치중, 해안쓰레기 정보 수집 및 빅데이터 생산이 이루어지지 않고 이에 따라 데이터에 근거한 관리가 이루어지지 않는 문제가 있습니다.

저희 팀은 **이러한 문제를 해결하고 바다환경지킴이 분들의 보다 편리한 수거 작업을 지원하는 플랫폼을 개발**하고자 하는 목표로 프로젝트를 기획하였습니다.

해당 프로젝트는 **글로벌 데이터 해커톤 DIVE 2024에 참여 및 발제사 '한국해양과학기술원 X 동아시아바다공동체'의 데이터를 기반으로 개발**하였으며 최종적으로 **발제사 1등상을 수상, 전체 경선에 진출하여 해커톤 대상을 수상**하였습니다.

## 🗓️ 프로젝트 일정

2024년 9월 18일 - 2024년 10월 06일 (2주)

<br>

## 🎇 주요 기능

채워넣어야지~

<br>

## 🛠 기술 스택
- **Backend**

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/Spring Data JPA-F05032?style=for-the-badge&logo=Spring&logoColor=white"> <img src="https://img.shields.io/badge/QueryDSL-81717?style=for-the-badge&logo=QueryDSL&logoColor=white"> <img src="https://img.shields.io/badge/ApacheJmeter-D22128?style=for-the-badge&logo=ApacheJmeter&logoColor=white">

- **Frontend**

<img src="https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white"> <img src="https://img.shields.io/badge/typescript-3178C6?style=for-the-badge&logo=typescript&logoColor=white">  <img src="https://img.shields.io/badge/ReactQuery-61DAFB?style=for-the-badge&logo=ReactQuery&logoColor=white">  <img src="https://img.shields.io/badge/NextAuth-339933?style=for-the-badge&logo=NextAuth&logoColor=white"> <img src="https://img.shields.io/badge/vercel-06B6D4?style=for-the-badge&logo=vercel&logoColor=white"> <img src="https://img.shields.io/badge/swr-007ACC?style=for-the-badge&logo=swr&logoColor=white"> <img src="https://img.shields.io/badge/chart JS-007ACC?style=for-the-badge&logo=chart JS&logoColor=white">

- **DB**
  
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> <img src="https://img.shields.io/badge/MongoDB-3178C6?style=for-the-badge&logo=MongoDB&logoColor=white">

- **Infra**

<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">   <img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white"> <img src="https://img.shields.io/badge/awselasticloadbalancing-6DB33F?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white">

- **Docs**

<img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">

<br>

## 📜설계
**🖋️아키텍처 구조도**

<img src="사진url" width="500">

**🖋️ERD**

![MSG-ERD](erd url)

**🖋️폴더구조(Back)**
```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂spharos
 ┃ ┃ ┃ ┗ 📂msg
 ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┣ 📂admin
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂converter
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂Impl
 ┃ ┃ ┃ ┃ ┃ ┣ 📂brand
 ┃ ┃ ┃ ┃ ┃ ┣ 📂bundle
 ┃ ┃ ┃ ┃ ┃ ┣ 📂cart
 ┃ ┃ ┃ ┃ ┃ ┣ 📂category
 ┃ ┃ ┃ ┃ ┃ ┣ 📂coupon
 ┃ ┃ ┃ ┃ ┃ ┣ 📂likes
 ┃ ┃ ┃ ┃ ┃ ┣ 📂options
 ┃ ┃ ┃ ┃ ┃ ┣ 📂orders
 ┃ ┃ ┃ ┃ ┃ ┣ 📂product
 ┃ ┃ ┃ ┃ ┃ ┣ 📂review
 ┃ ┃ ┃ ┃ ┃ ┣ 📂search
 ┃ ┃ ┃ ┃ ┃ ┗ 📂users
 ┃ ┃ ┃ ┃ ┣ 📂global
 ┃ ┃ ┃ ┃ ┃ ┣ 📂api
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂code
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂status
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂example
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂converter
 ┃ ┃ ┃ ┃ ┃ ┣ 📂database
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┣ 📂redis
 ┃ ┃ ┃ ┃ ┃ ┗ 📂security
 ┃ ┃ ┃ ┃ ┗ 📜MsgApplication.java
 ┃ ┗ 📂resources
 ┗ 📂test

```
**🖋️폴더구조(Front)**
```
📦components
 ┣ 📂banner
 ┣ 📂form
 ┣ 📂layout
 ┣ 📂MainCategory
 ┣ 📂pages
 ┃ ┣ 📂auth
 ┃ ┃ ┣ 📂signup
 ┃ ┃ ┗ 📂users
 ┃ ┣ 📂cart
 ┃ ┣ 📂category
 ┃ ┣ 📂catogory-list
 ┃ ┣ 📂join
 ┃ ┣ 📂like
 ┃ ┣ 📂login
 ┃ ┣ 📂main
 ┃ ┣ 📂nonuser
 ┃ ┣ 📂order
 ┃ ┃ ┣ 📂order-complete
 ┃ ┃ ┗ 📂product-order
 ┃ ┣ 📂product-detail
 ┃ ┣ 📂product-list
 ┃ ┃ ┣ 📂ranking
 ┃ ┃ ┣ 📂special-price
 ┃ ┣ 📂product-review
 ┃ ┣ 📂search
 ┃ ┗ 📂users
 ┃ ┃ ┗ 📂my-order
 ┗ 📂ui
```

**🖋️협업 관리**

그라운드룰 | 노션 기반 문서공유 |
--- | --- | 
![그라운드룰](https://github.com/1-MSG/backend/assets/122415843/b3010e34-dd6b-4c25-82ba-758b62a113f3)| ![노션_기반_진행상황_공유](https://github.com/1-MSG/backend/assets/122415843/8bc0838a-bc2e-41bc-95b0-fcc965302ac0) |

데일리 스크럼 및 회고 | Gitflow 전략 및 통일된 커밋 메시지 |
--- | --- | 
![데일리_스크럼_및_회고](https://github.com/1-MSG/backend/assets/122415843/5c3c2f6a-9ab8-4015-9768-e216cfffd9a4)| !![Gitflow_전략_및_통일된_커밋_메시지](https://github.com/1-MSG/backend/assets/122415843/6fc761ce-7ebd-47ba-8110-b6bd64dcd4cf) |

코드리뷰 | 트러블 슈팅 |
--- | --- |
![코드리뷰](https://github.com/1-MSG/.github/assets/122415843/dde2650b-ba16-40bc-ac82-95ffe0bf1e48) | ![트러블슈팅](https://github.com/1-MSG/.github/assets/122415843/cdee45e3-9f06-49ee-8c71-56791888bfbd) |

<br>

## 👥팀원 소개
|   <img src="https://avatars.githubusercontent.com/u/122415843?v=4" width="40" />   |   <img src="https://avatars.githubusercontent.com/u/81681883?v=4" width="40" />    |
| :----: | :----: |
| 서이현 | 김은비 |
| BackEnd| BackEnd|
| tjwn1408@naver.com  |eunbi@gmail.com  |
| [@I-HYEON](https://github.com/I-HYEON) | [@eunbi](깃허브주소)  |
