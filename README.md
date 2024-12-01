# 바다환경지킴이 사업을 위한 통합 플랫폼, 바다지킴이 🐳
<br>
<p align="center"><img src="https://github.com/user-attachments/assets/231ac93d-c6ba-43fc-a188-0c9732c40747" width="200"></p>
<br>

## 👩‍👧‍👦 기획 배경
'바다환경지킴이 사업'은 **전국 해안을 접한 기초 지자체마다 상시 전담관리 인력인 바다환경지킴이를 배치하여 해안가를 순회하며 쓰레기를 수거하는 사업**입니다. (24년도 기준) 지금까지의 청소 프로세스는 단순 수거 목적에 치중, 해안쓰레기 정보 수집 및 빅데이터 생산이 이루어지지 않고 이에 따라 데이터에 근거한 관리가 이루어지지 않는 문제가 있습니다.

저희 팀은 **이러한 문제를 해결하고 바다환경지킴이 분들의 보다 편리한 수거 작업을 지원하는 플랫폼을 개발**하고자 하는 목표로 프로젝트를 기획하였습니다.

<br>

## 🗓️ 프로젝트 일정

2024년 9월 18일 - 2024년 10월 06일 (2주)

해당 프로젝트는 **글로벌 데이터 해커톤 DIVE 2024에 참여** 및 발제사 '한국해양과학기술원 X 동아시아바다공동체'의 데이터를 기반으로 개발하였으며 최종적으로 **발제사 1등상을 수상, 전체 경선에 진출하여 해커톤 대상을 수상**하였습니다.

<br>
<br>

## 🎇 주요 기능

1. 회원가입 및 로그인
- 회원가입 후 로그인 할 수 있습니다.
- 바다환경지킴이 분들은 일반 사용자(USER) 상태로 가입됩니다. 관리자의 경우, 별도로 계정을 생성하여 관리자(ADMIN) 상태로 가입됩니다.
- 주 사용자층(바다환경지킴이)의 연령층을 고려하여 큰글씨 모드를 제공합니다.
<br>

| <img src="https://github.com/user-attachments/assets/176c92e3-0c5c-4edd-9292-84daa5b34267" width="160" /> | <img src="https://github.com/user-attachments/assets/20916640-0601-4128-81e6-528efaf50751" width="160" /> | <img src="https://github.com/user-attachments/assets/6d4cd26e-d5bd-48ed-8c12-2b3669cf8e17" width="160" /> | <img src="https://github.com/user-attachments/assets/ebb6b738-04a1-4d04-9ccd-b8376ed0898d" width="160" /> |
| :----: | :----: | :----: | :----: |
| 회원가입 및 로그인 화면 | 로그인 상태 확인 | 조사/청소/관리/수거 모드 | 큰글씨 모드 |

<br>

2. 조사모드
- 조사모드는 해안을 탐사 후 예측한 쓰레기 위치와 양을 기록하는 공간입니다.
- 해안명, 해안 길이, 쓰레기량, 해안 탐사 사진 등을 기록할 수 있습니다. 위치와 시간은 자동 기록됩니다.
- 바다환경지킴이는 자신이 기록한 조사 목록을 조회할 수 있습니다. 관리자는 모든 조사 데이터를 확인할 수 있습니다.
<br>

| <img src="https://github.com/user-attachments/assets/6d4cd26e-d5bd-48ed-8c12-2b3669cf8e17" width="200" /> | <img src="https://github.com/user-attachments/assets/3ab80f39-d7b4-441a-a1e1-84f0cd0a7bf3" width="200" /> | <img src="https://github.com/user-attachments/assets/03cd0e32-99a0-415a-9653-d5a5ec63eac3" width="200" /> |
| :----: | :----: | :----: |
| 조사모드 진입 | 조사 데이터 작성 | 목록 조회 |

<br>

3. 청소모드
- 청소모드는 해안을 청소 후 실제 수거한 쓰레기 위치와 양을 기록하는 공간입니다.
- 해안명, 해안 길이, 쓰레기량, 사진 등을 기록할 수 있습니다. 위치와 시간은 자동 기록됩니다.
- 바다환경지킴이는 자신이 기록한 청소 목록을 조회할 수 있습니다. 관리자는 모든 청소 데이터를 확인할 수 있습니다.
<br>

| <img src="https://github.com/user-attachments/assets/6d4cd26e-d5bd-48ed-8c12-2b3669cf8e17" width="200" /> | <img src="https://github.com/user-attachments/assets/bd35123d-3966-4d19-ba4e-e4e84c215753" width="200" /> | <img src="https://github.com/user-attachments/assets/03cd0e32-99a0-415a-9653-d5a5ec63eac3" width="200" /> |
| :----: | :----: | :----: |
| 청소모드 진입 | 청소 데이터 작성 | 목록 조회 |

<br>

4. 관리모드
- 관리모드는 쌓인 청소 데이터를 시각화하여 관리자를 돕는 공간입니다
- 관리자는 원하는 기간, 통계 유형을 선택하고 데이터 분포를 확인합니다.
- 지도 상에서 동심원 색상은 쓰레기 유형을, 동심원 크기는 쓰레기 수거량을 의미합니다.
- 관리자는 해당 데이터를 엑셀 형식으로 다운로드 받을 수 있습니다.
<br>

| <img src="https://github.com/user-attachments/assets/112af02c-b44f-460e-afbe-1a59c101d542" width="160" /> | <img src="https://github.com/user-attachments/assets/dbd466a5-de2f-436e-a7d2-dbd5f499203c" width="160" /> | <img src="https://github.com/user-attachments/assets/32ecfc31-1064-417e-a978-d3e99eeee573" width="160" /> | <img src="https://github.com/user-attachments/assets/78c768e6-fdfe-4582-96e2-dd4c18f9da65" width="160" /> |
| :----: | :----: | :----: |:----: |
| 관리모드 진입 | 기간 내 수거량 조회</br>- 동심원 색상: 쓰레기 유형</br>- 동심원 크기: 쓰레기 수거량 | 데이터 다운로드 버튼 클릭 | 엑셀 파일 확인
<br>

5. 수거모드(관리자)
- 수거모드에서 관리자는 아직 수거되지 않은 쓰레기 데이터를 지도에서 전부 확인할 수 있습니다
- 쓰레기통 아이콘은 줌 레벨에 따라 상세 정보를 보여줍니다.
- 이 때, 하얀색 아이콘은 아직 담당자가 없는 쓰레기, 초록색 아이콘은 담당자가 지정되어 수거가 진행 중임을 의미합니다.
- 관리자는 여러 아이콘을 클릭하여 담은 후, 담당자를 배정할 수 있습니다.
<br>

| <img src="https://github.com/user-attachments/assets/31871448-c6e9-49d9-a45e-ba2c5478edd1" width="200" /> | <img src="https://github.com/user-attachments/assets/a56fa544-7b38-4362-b2ed-0b20c68ad02a" width="200" /> | <img src="https://github.com/user-attachments/assets/5ac4ae33-e0d5-4970-a9ac-c9b3a8c57d0f" width="200" /> |
| :----: | :----: | :----: |
| 쓰레기 데이터 3개 선택 | 담당자 선택 후 배정 | 전화 아이콘 클릭</br>사용자 번호를 기반으로</br>스마트폰의 전화 기능 연결 |

6. 수거모드(바다환경지킴이)
- 수거모드에서 사용자는 자신에게 배정된 쓰레기 위치만을 확인할 수 있습니다.
- 원하는 지점을 클릭하여 경로를 조회합니다. kakao mobility API를 기반으로 거리, 교통상황, 통행료 등이 제공됩니다.
- 우측 중앙의 체크 아이콘을 통해 수거 완료한 쓰레기를 지도에서 제거할 수 있습니다.(수거 상태 업데이트)
<br>

| <img src="https://github.com/user-attachments/assets/ca4d5295-138f-49cb-91be-a13b01b9cee8" width="160" /> | <img src="https://github.com/user-attachments/assets/ee604c76-d4d7-43e8-bfc7-0851199817ed" width="160" /> | <img src="https://github.com/user-attachments/assets/6d8566c6-8cf8-4e30-8f9d-11c51fa21063" width="160" /> | <img src="https://github.com/user-attachments/assets/65d5142d-2d0e-41a9-909f-4944cf88500e" width="160" /> |
| :----: | :----: | :----: | :----: |
| 원하는 지점 클릭 | 경로 조회 결과 | 수거 완료 처리 | 완료된 데이터 지도에서 제거

<br>

## 🛠 기술 스택
- **Backend**

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> <img src="https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white">  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> <img src="https://img.shields.io/badge/Spring Data JPA-F05032?style=for-the-badge&logo=Spring&logoColor=white"> 

- **Frontend**

<img src="https://img.shields.io/badge/typescript-3178C6?style=for-the-badge&logo=typescript&logoColor=white"> <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"> <img src="https://img.shields.io/badge/ReactQuery-61DAFB?style=for-the-badge&logo=ReactQuery&logoColor=white"> <img src="https://img.shields.io/badge/leaflet-199900?style=for-the-badge&logo=leaflet&logoColor=white"> 

- **Infra**

<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">   <img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white"> <img src="https://img.shields.io/badge/awselasticloadbalancing-6DB33F?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white"> <img src="https://img.shields.io/badge/vercel-06B6D4?style=for-the-badge&logo=vercel&logoColor=white">

- **Docs**

<img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">

<br>

## 🖋️ 설계
**📜 아키텍처 구조도**

<img src="https://github.com/user-attachments/assets/672c30c4-1fb3-4078-950e-f5606b8c8002" width="800">

**📜 ERD**

<img src="https://github.com/user-attachments/assets/808cd66a-d23b-4ee4-9ceb-6e05f2d71e72" width="700">

<br>

**📜 폴더구조(Back)**

```
📦main
 ┣ 📂java
 ┃ ┗ 📂team
 ┃ ┃ ┗ 📂ivy
 ┃ ┃ ┃ ┗ 📂oceanguardian
 ┃ ┃ ┃ ┃ ┣ 📂domain
 ┃ ┃ ┃ ┃ ┃ ┣ 📂admin
 ┃ ┃ ┃ ┃ ┃ ┣ 📂cleanup
 ┃ ┃ ┃ ┃ ┃ ┣ 📂image
 ┃ ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┗ 📂monitoring
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┣ 📂global
 ┃ ┃ ┃ ┃ ┃ ┣ 📂apiresponse
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┗ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂errorcode
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂erroresponse
 ┃ ┃ ┃ ┃ ┗ 📜OceanguardianApplication.java
 ┗ 📂resources
 ┃ ┣ 📂static
 ┃ ┣ 📂templates
 ┃ ┗ 📜application.yml

```
**📜 폴더구조(Front)**
```
📦src
 ┣ 📂api
 ┣ 📂assets
 ┃ ┣ 📂image
 ┣ 📂components
 ┃ ┣ 📂Cleaner
 ┃ ┣ 📂Collector
 ┃ ┣ 📂Common
 ┃ ┣ 📂Home
 ┃ ┣ 📂Inspector
 ┃ ┗ 📂Manager
 ┣ 📂pages
 ┃ ┣ 📂Cleaner
 ┃ ┣ 📂Collector
 ┃ ┣ 📂Inspector
 ┃ ┣ 📂Manager
 ┃ ┣ 📂User
 ┣ 📂recoil
 ┣ 📂utils
 ┣ 📜App.css
 ┣ 📜App.tsx
 ┣ 📜index.css
 ┣ 📜main.tsx
 ┗ 📜vite-env.d.ts
```

## 👥팀원 소개
|   <img src="https://avatars.githubusercontent.com/I-HYEON" width="40" />   |   <img src="https://avatars.githubusercontent.com/yoon-b" width="40" />    |
| :----: | :----: |
| 서이현 | 김은비 |
| BackEnd 개발 및 배포 | FrontEnd 개발 및 배포 |
| tjwn1408@naver.com  | eunbi.kim.kor@gmail.com |
| [@I-HYEON](https://github.com/I-HYEON) | [@yoon-b](https://github.com/yoon-b)  |
