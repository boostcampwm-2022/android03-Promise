![Readme 배경](https://user-images.githubusercontent.com/61190129/205502433-c4042a7e-9332-43e3-ac15-7965e2d52ff9.png)

<div align="center"><img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white">
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white"></div>

## 🚀 프로젝트 소개

### 위치 정보를 활용한 **약속 관리 앱**

👬 만나고 싶은 친구들을 초대해보세요

📅 캘린더에서 내 약속을 관리할 수 있어요

📍 약속장소에 오기로 한 친구들이 어디쯤에 있는지 확인할 수 있어요

⏰ 약속시간이 다가오면 알림을 받을 수 있어요

<br>

## 🧑‍💻 팀원

| K012                                | K020                              | K025                             | K028                               |
|-------------------------------------|-----------------------------------|----------------------------------|------------------------------------|
| [류대현](https://github.com/jerrytrap) | [박찬호](https://github.com/hoho-97) | [양성현](https://github.com/dogeby) | [유수미](https://github.com/Yoo-sumi) |

<br>

## ✏️ 기술 스택

| Category             | Tech Stack                                                                              |
|----------------------|-----------------------------------------------------------------------------------------|
| Language             | <img src="https://img.shields.io/badge/Kotlin-FAF8F1? style=bold&logo=Kotlin&logoColor=#7F52FF"/>  |
| Architecture         | <img src="https://img.shields.io/badge/MVVM-FAF8F1? style=flat"/>  <img src="https://img.shields.io/badge/DataBinding-FAF8F1? style=flat"/> |
| Async Task           | <img src="https://img.shields.io/badge/Kotlin Coroutine-FAF8F1? style=flat&logo=Kotlin&logoColor=#7F52FF"/>  <img src="https://img.shields.io/badge/Kotlin Flows-FAF8F1? style=flat&logo=Kotlin&logoColor=#7F52FF"/>                                                                |
| API                  | <img src="https://img.shields.io/badge/Fused Location Provider API-FAF8F1? style=flat&logo=Google-Maps&logoColor=#4285F4"/> <img src="https://img.shields.io/badge/Firebase Cloud Messaging-FAF8F1? style=flat&logo=Firebase&logoColor=#FFCA28"/> <img src="https://img.shields.io/badge/Naver Maps API-FAF8F1? style=flat&logo=Naver&logoColor=#03C75A"/> <img src="https://img.shields.io/badge/Naver Search API-FAF8F1? style=flat&logo=Naver&logoColor=#03C75A"/> |
| Background Task      | <img src="https://img.shields.io/badge/AlarmManager-FAF8F1? style=flat&logo=Android&logoColor=#3DDC84"/>  |
| Database             | <img src="https://img.shields.io/badge/Cloud FireStore-FAF8F1? style=flat&logo=Firebase&logoColor=#FFCA28"/> <img src="https://img.shields.io/badge/Room-FAF8F1? style=flat&logo=Android&logoColor=#3DDC84"/> <img src="https://img.shields.io/badge/Preferences DataStore-FAF8F1? style=for-the-badge&logo=Android&logoColor=#3DDC84"/> |
| External Library     | <img src="https://img.shields.io/badge/Material Calendar View-FAF8F1? style=flat"/> |
| Dependency Injection | <img src="https://img.shields.io/badge/Hilt-FAF8F1? style=flat&logo=Android&logoColor=#3DDC84"/> |
| Network              | <img src="https://img.shields.io/badge/Retrofit2-FAF8F1? style=flat"/>                  |

<br>

## 💡 주요 기능

| 화면             | 기능                                                                              |
|----------------------|-----------------------------------------------------------------------------------------|
|<img width="270" src="https://user-images.githubusercontent.com/68229193/207355078-6f9399c7-cbca-4afa-a6a0-d03ba3d2d3bc.png"/>|📆**약속 캘린더**<br>사용자의 약속 일정들을 달력으로 확인<br><br><br>◾ 직접 추가하거나 초대된 약속을 날짜별로 확인할 수 있어요.<br>&nbsp;&nbsp;◽ 약속 관리로 추가, 삭제되는 약속이 실시간 방영돼요.<br><br><br>◾ 약속이 있는 날을 달력에서 미리 확인할 수 있어요.<br><br><br>◾ 약속의 장소나 시간을 미리 확인할 수 있어요.|
|<img width="270" src="https://user-images.githubusercontent.com/67852426/207547789-f69257a0-aa25-4668-bbcc-22dbd23f5b46.png"/>|👭**친구 목록**<br>사용자가 추가한 친구 목록 확인<br><br><br>◾ 검색 탭에서 다른 사용자를 검색할 수 있어요.<br><br><br>◾ 다른 사용자를 추가해 내 친구 목록에 추가할 수 있어요.<br><br><br>◾ 친구 탭에서 내가 추가한 친구 목록을 확인할 수 있어요.|
|<img width="270" src="https://user-images.githubusercontent.com/68229193/207356466-7984ee49-096e-4056-82b1-5f78b0c22056.png"/>|🤙**약속 생성**<br>만나고 싶은 친구들을 초대해 약속 생성할 수 있어요.<br><br><br>◾ 추가할 약속을 생성할 수 있어요.<br>&nbsp;&nbsp;◽약속 제목, 날짜, 시간, 장소를 선택합니다.<br>&nbsp;&nbsp;◽약속 멤버를 초대합니다.<br><br><br>◾ 기존 약속 정보를 수정할 수 있어요.<br><br><br>◾ 약속을 삭제할 수 있어요.<br>&nbsp;&nbsp;◽수정, 삭제된 약속은 모든 멤버에게 동일하게 반영돼요.|
|<img width="270" src="https://user-images.githubusercontent.com/68229193/207357028-6ea4e624-bfae-4acb-bce8-f15ce61c1515.png"/>|📍**실시간 친구 위치 확인**<br>약속 친구 멤버들의 위치를 실시간으로 조회<br><br><br>◾ 위치 공유로 약속 멤버들의 실시간 위치를 확인할 수 있어요.<br>&nbsp;&nbsp;◽멤버 중 위치 공유를 켜놓은 사용자만 확인이 가능해요.<br>&nbsp;&nbsp;◽위치 공유를 끈다면 다른 멤버 위치는 확인할 수 없어요.<br><br><br>◾ 아이콘을 터치하면 적절한 위치로 지도가 이동해요.<br>&nbsp;&nbsp;◽깃발 아이콘으로 목적지를 보여줘요.<br>&nbsp;&nbsp;◽멤버 아이콘으로 해당 멤버 위치를 보여줘요.<br>&nbsp;&nbsp;◽돋보기 아이콘으로 전체 멤버 위치를 보여줘요.|
|<img width="270" src="https://user-images.githubusercontent.com/68229193/207357367-29d6a573-f35d-4d4b-b238-8d66642ee96d.png"/>|⏰**약속 알리미**<br>약속을 더 편리하게 관리하기 위한 알림<br><br><br>◾ 초대된 약속에 대한 알림을 받아볼 수 있어요.<br>&nbsp;&nbsp;◽약속이 추가/수정/삭제되었을 때<br>&nbsp;&nbsp;◽약속 시간이 임박할 때(1시간 전)|

## 📝 개발 일지

- [Naver Maps API 사용기](https://github.com/boostcampwm-2022/android03-Promise/wiki/Naver-Maps-API-%EC%82%AC%EC%9A%A9%EA%B8%B0)
- [AlarmManager](https://github.com/boostcampwm-2022/android03-Promise/wiki/AlarmManager)
- [Firebase Cloud Messaging](https://github.com/boostcampwm-2022/android03-Promise/wiki/Firebase-Cloud-Messaging)
- [MVVM](https://github.com/boostcampwm-2022/android03-Promise/wiki/MVVM)
- [Location API 선택하기](https://github.com/boostcampwm-2022/android03-Promise/wiki/Location-API-%EC%84%A0%ED%83%9D%ED%95%98%EA%B8%B0)
- [Activity와 Service 상호작용하기](https://github.com/boostcampwm-2022/android03-Promise/wiki/Activity%EC%99%80-Service-%EC%83%81%ED%98%B8%EC%9E%91%EC%9A%A9%ED%95%98%EA%B8%B0)
- [상세화면 ViewModel 리펙토링](https://github.com/boostcampwm-2022/android03-Promise/wiki/%EC%83%81%EC%84%B8%ED%99%94%EB%A9%B4-ViewModel-%EB%A6%AC%ED%8E%99%ED%86%A0%EB%A7%81)
