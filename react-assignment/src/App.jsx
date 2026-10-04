/* ---------------------------------------------------------
   App.jsx — book_ecma 의 main.js 자리
   화면을 직접 고치는 대신, 값을 바꾸면 화면이 따라오게 한다.
   --------------------------------------------------------- */

// 이 줄이 없으면 스타일이 하나도 먹지 않는다.
import "./style.css";

import { useEffect, useState } from "react";

// 서버와 대화하는 함수 — book_ecma 의 파일을 그대로 쓴다.
import { fetchBooks } from "./api/bookApi.js";

// 폼 값과 서버 데이터 사이의 변환
import { EMPTY_FORM } from "./lib/bookData.js";

// 화면 조각
import BookForm from "./components/BookForm.jsx";
import BookTable from "./components/BookTable.jsx";

// 컴포넌트 이름은 대문자로 시작한다. 소문자로 쓰면 React 가 HTML 태그로 본다.
function App() {
    /* 화면을 이루는 값 일곱 개
       useState 는 [지금 값, 값을 바꾸는 함수] 두 개를 돌려준다.
       바꾸는 함수를 부르면 React 가 컴포넌트를 다시 실행해 화면을 새로 그린다.
       book_ecma 에서 DOM 에 흩어져 있던 상태가 여기 모였다. */
    const [books, setBooks] = useState([]);              // 표에 그릴 도서 목록
    const [form, setForm] = useState(EMPTY_FORM);        // 입력칸 11개의 값
    const [editingId, setEditingId] = useState(null);    // null 이면 등록 모드, 값이 있으면 수정 모드
    const [loading, setLoading] = useState(false);       // "로딩 중..." 을 보일까
    const [listError, setListError] = useState(null);    // 표 자리에 낼 오류 문구
    const [message, setMessage] = useState(null);        // 폼 아래 메시지 — { text, type } 또는 null
    const [detailBook, setDetailBook] = useState(null);  // 상세 보기로 고른 도서. null 이면 안 그린다

    // 수정 모드인지는 editingId 로 알 수 있으므로 따로 state 를 두지 않는다.
    const isEditing = editingId !== null;

    /* 목록 불러오기 — book_ecma main.js 의 loadBooks 를 옮겼다.
       여기의 setLoading 은 ui/message.js 의 함수가 아니라
       useState 가 돌려준 state 변경 함수다. */
    async function loadBooks() {
        setLoading(true);
        setListError(null);

        try {
            const data = await fetchBooks();

            // book_ecma 에서는 renderBookTable(books) 를 불렀다.
            // 여기서는 값만 바꾸면 React 가 화면을 다시 그린다.
            setBooks(data);
        } catch (error) {
            console.error("Error:", error);
            setMessage({ text: error.message, type: "error" });
            // renderTableError() 대신 오류 문구를 state 에 담는다.
            setListError("오류: 데이터를 불러올 수 없습니다.");
        } finally {
            // 성공하든 실패하든 로딩 표시는 반드시 끈다.
            setLoading(false);
        }
    }

    /* 처음 한 번만 목록을 불러온다.
       book_ecma 의 main.js 맨 아래에 적었던 loadBooks() 한 줄에 해당한다.
       두 번째 인자 [] 가 "처음 한 번만" 이라는 뜻이다.
       빠뜨리면 화면을 그릴 때마다 실행되어 서버 요청이 끝없이 반복된다. */
    useEffect(() => {
        // 아래 주석은 ESLint 에게 "이 경고는 알고 있다" 고 알려 주는 줄이다.
        // eslint-disable-next-line react-hooks/set-state-in-effect -- 처음 한 번 목록을 불러오는 것은 의도된 동작입니다
        loadBooks();
    }, []);

    /* 입력칸 한 개가 바뀔 때
       입력칸 11개가 모두 이 함수 하나를 부른다.
       어느 칸인지는 event.target.name 이 알려 준다.

       state 는 직접 고치지 않는다. form.title = ... 처럼 고치면
       React 는 "같은 객체" 로 보아 화면을 다시 그리지 않는다.
       전개 구문(...form)으로 기존 값을 복사한 새 객체를 만들고,
       [name] (계산된 속성명)으로 바뀐 칸 하나만 덮어쓴다. */
    function handleChange(event) {
        const { name, value } = event.target;

        setForm({ ...form, [name]: value });
    }

    // 폼 제출과 취소는 과제 8 에서 채운다.
    // 지금은 제출할 때 페이지가 새로고침되는 것만 막아 둔다.
    function handleSubmit(event) {
        event.preventDefault();
    }

    function resetForm() {}

    /* 표의 버튼이 눌렸을 때 부를 함수들.
       속은 과제 9 · 10 에서 채운다. 지금은 표를 먼저 확인하기 위해 비워 둔다.
       쓰지 않을 매개변수는 적지 않는다. BookTable 이 onEdit(book.id) 로 불러도
       받지 않은 인자는 자바스크립트가 그냥 버린다. */
    function handleEdit() {}

    function handleDelete() {}

    function handleDetail() {}

    // 태그 여러 개를 나란히 돌려줄 수 없으므로 프래그먼트(<> </>)로 감싼다.
    return (
        <>
            <h1>도서 관리 시스템</h1>

            <BookForm
                form={form}
                isEditing={isEditing}
                message={message}
                onChange={handleChange}
                onSubmit={handleSubmit}
                onCancel={resetForm}
            />

            <BookTable
                books={books}
                loading={loading}
                error={listError}
                onEdit={handleEdit}
                onDelete={handleDelete}
                onDetail={handleDetail}
            />
        </>
    );
}

// 다른 파일(main.jsx)에서 이름 없이 가져다 쓸 수 있게 내보낸다.
export default App;
