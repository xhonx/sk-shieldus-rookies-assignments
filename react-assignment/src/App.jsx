/* ---------------------------------------------------------
   App.jsx — book_ecma 의 main.js 자리
   화면을 직접 고치는 대신, 값을 바꾸면 화면이 따라오게 한다.
   --------------------------------------------------------- */

// 이 줄이 없으면 스타일이 하나도 먹지 않는다.
import "./style.css";

import { useState } from "react";

// 컴포넌트 이름은 대문자로 시작한다. 소문자로 쓰면 React 가 HTML 태그로 본다.
function App() {
    /* 화면을 이루는 값 일곱 개
       useState 는 [지금 값, 값을 바꾸는 함수] 두 개를 돌려준다.
       바꾸는 함수를 부르면 React 가 컴포넌트를 다시 실행해 화면을 새로 그린다.
       book_ecma 에서 DOM 에 흩어져 있던 상태가 여기 모였다. */
    const [books, setBooks] = useState([]);              // 표에 그릴 도서 목록
    const [form, setForm] = useState({});                // 입력칸 11개의 값 (과제 6 에서 EMPTY_FORM 으로 바꾼다)
    const [editingId, setEditingId] = useState(null);    // null 이면 등록 모드, 값이 있으면 수정 모드
    const [loading, setLoading] = useState(false);       // "로딩 중..." 을 보일까
    const [listError, setListError] = useState(null);    // 표 자리에 낼 오류 문구
    const [message, setMessage] = useState(null);        // 폼 아래 메시지 — { text, type } 또는 null
    const [detailBook, setDetailBook] = useState(null);  // 상세 보기로 고른 도서. null 이면 안 그린다

    return <h1>도서 관리 시스템</h1>;
}

// 다른 파일(main.jsx)에서 이름 없이 가져다 쓸 수 있게 내보낸다.
export default App;
