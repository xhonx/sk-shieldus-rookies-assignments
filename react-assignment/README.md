# book_react_first — 도서 관리 시스템 (React + Vite)

SK쉴더스 루키즈 6기 프론트엔드 과제입니다.
ECMAScript 로 작성된 도서 관리 시스템 [book_ecma](https://github.com/mysoyul/Html_CSS_JS_App/tree/main/book_ecma) 를
기능은 그대로 둔 채 **React** 로 다시 씁니다. 바뀌는 것은 "화면을 바꾸는 방법" 하나입니다.

## 실행

```bash
npm install       # 처음 한 번
npm run dev       # 개발 서버 (http://localhost:5173)
npm run build     # 배포용 빌드 → dist/
npm run preview   # 빌드 결과 확인
```

Spring Boot 서버가 `http://localhost:8080` 에서 실행 중이어야 합니다.
주소는 `.env.development` 의 `VITE_API_BASE_URL` 로 바꿀 수 있습니다.

## book_ecma 에서 그대로 가져온 파일

| book_ecma | book_react_first | 비고 |
| --- | --- | --- |
| `src/config.js` | `src/config.js` | 그대로 복사 |
| `src/api/bookApi.js` | `src/api/bookApi.js` | 그대로 복사 |
| `src/lib/validation.js` | `src/lib/validation.js` | 그대로 복사 |
| `src/style.css` | `src/style.css` | 그대로 복사 (과제 7 에서 한 줄만 지운다) |
| `.env.development` | `.env.development` | 그대로 복사 |
| `.env.production` | `.env.production` | 그대로 복사 |

## 과제 목록

| 과제 | 무엇을 하나 | 주로 쓰는 React |
| --- | --- | --- |
| 1 | React 프로젝트 만들기와 그대로 옮길 것 고르기 | npm create vite, 모듈 재사용 |
| 2 | 첫 화면 만들기 — JSX 와 컴포넌트 | JSX, 컴포넌트, export default |
| 3 | 화면을 값으로 바꾸기 | useState — 값 일곱 개 |
| 4 | 목록 불러오기 | useEffect, 의존성 배열 |
| 5 | 표를 컴포넌트로 | map, key, props |
| 6 | 폼을 제어 컴포넌트로 | value + onChange, 계산된 속성명 |
| 7 | 메시지와 로딩 — 조건부 렌더링 | && 와 삼항, null 반환, 정리 함수 |
| 8 | 등록과 수정 — 폼 제출 | onSubmit, 상태 되돌리기 |
| 9 | 수정 준비와 삭제 | props 로 함수 넘기기, useRef |
| 10 | 상세 보기 — alert 을 컴포넌트로 | 조건부 렌더링, null 반환 |
| 11 | App.jsx 로 조립하기 | 컴포넌트 구성 |
| 12 | 빌드와 배포 | npm run build |
| 13 | 리팩토링 — 반복되는 입력칸을 공통 컴포넌트로 | 컴포넌트 재사용, props |

## 빌드 결과 (과제 12)

`npm run build` 로 만든 결과물을 book_ecma 와 견주어 본 값입니다.

| 항목 | book_ecma | book_react_first |
| --- | --- | --- |
| js 크기 | 6.47 kB (gzip 2.86 kB) | 229.90 kB (gzip 71.66 kB, React 포함) |
| css 크기 | 2.51 kB | 2.86 kB |
| index.html | 3.68 kB (화면 전체) | 0.47 kB (빈 상자 하나) |
| 읽는 .env | `.env.production` | `.env.production` (같음) |
| 모듈 수 | 12개 | 24개 (React 라이브러리 포함) |

- `npm run preview` 로 열면 제목 옆 배지가 빨간 **PROD** 로 바뀝니다.
- `dist/assets` 의 js 파일 안에 `.env.production` 의 주소(`https://api.myservice.com`)가 그대로 들어 있습니다.
  숨겨야 하는 값에는 `VITE_` 를 붙이면 안 됩니다.
- 개발 모드에서는 StrictMode 때문에 목록 요청이 두 번 나가고, 빌드 결과에서는 한 번 나갑니다.
- `.env.production` 의 `VITE_API_BASE_URL` 은 예시 주소이므로, 실제 서버 주소로 바꾼 뒤 빌드해야 목록이 조회됩니다.
