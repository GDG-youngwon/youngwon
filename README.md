# 📝 블로그 좋아요 + 댓글 기능 통합 구현

> GDGoC Sookmyung 2025-02-Spring-Novice-Study 토이 프로젝트  

> Spring Boot 블로그 애플리케이션에 좋아요 기능과 댓글 기능을 통합 구현 

---

## 🛠 코드 수정사항 및 트러블슈팅 정리

- 💗 [좋아요 기능 Postman 테스트 기록 & Source Code - 이채영](https://www.notion.so/9-Postman-2025-11-26-2b951b5834bc8074ac33d8679081df37?source=copy_link)  
- 💬 [댓글 기능 Source Code 통합 정리 - 이주원](https://worried-pancreas-db7.notion.site/Source-code-2b71986ccbfc80c8a937dc4b231d2e2f?pvs=143)

---

## 💡 구현 기능 요약

| 기능 항목 | 설명 |
|-----------|------|
| 좋아요 기능 | 게시글별 좋아요 토글 (🤍/❤️), 사용자별 중복 방지 |
| 로그인 상태 체크 | 로그인 사용자만 좋아요 및 댓글 가능 |
| 좋아요 실시간 반영 | JS Fetch API 기반 Ajax 처리 + DOM 동기화 |
| 댓글 기능 | 댓글 작성/삭제 기능 + 목록 출력 |
| 조건부 안내문구 | 댓글이 없을 때 "첫 댓글을 남겨보세요 ✨" 표시 |
| 권한 기반 UI 제어 | 수정/삭제 버튼은 작성자에게만 노출 |

---

## ✨ 실행 화면 캡처

### 1. 게시글 목록 페이지

<img width="1425" height="568" alt="좋아요 클릭 UI" src="https://github.com/user-attachments/assets/04474b67-5978-40f0-b59f-59f909410b23" />

### 2. 좋아요 클릭

<img width="976" height="654" alt="댓글 없음 상태" src="https://github.com/user-attachments/assets/09aad94d-dba9-431d-843b-bbea07b75c6e" />

### 3. 댓글 등록 후

<img width="982" height="750" alt="댓글 등록 후" src="https://github.com/user-attachments/assets/e7d21cb8-cbe1-4af2-9e08-47e397975a09" />

### 4. 좋아요 미클릭

<img width="969" height="658" alt="좋아요 없음" src="https://github.com/user-attachments/assets/a08b17e2-0785-4422-b77a-1df05d9e6f47" />

---

## 테스트 체크리스트

- [x] 로그인 시 좋아요 버튼 보이고 동작하는가  
- [x] 좋아요 수 증가/감소가 실시간 반영되는가  
- [x] 비로그인 시 좋아요 버튼이 보이지 않는가  
- [x] 댓글 작성 후 바로 반영되는가  
- [x] 댓글 삭제 시 정상 제거되는가  
- [x] 댓글 없을 때 안내 문구 출력되는가  
- [x] 게시글 작성자에게만 수정/삭제 버튼 보이는가  
