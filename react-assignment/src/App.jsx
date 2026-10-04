/* ---------------------------------------------------------
   App.jsx — book_ecma 의 main.js 자리
   이 앱의 유일한 컨테이너 컴포넌트입니다. 값을 갖고, 서버를 부르고,
   자식이 알려 온 일을 처리합니다. 화면을 그리는 일은 자식에게 맡깁니다.

   하는 일은 main.js 와 같습니다. 달라진 것은 "화면을 바꾸는 방법" 하나입니다.

     book_ecma : renderBookTable(books)  — 내가 DOM 을 고친다
     React     : setBooks(books)         — 값만 바꾸면 React 가 다시 그린다

   그래서 이 파일에는 document 가 한 번도 나오지 않습니다.
   --------------------------------------------------------- */

/* ── 1. 가져오기 ────────────────────────────────────────── */

// 이 줄이 없으면 스타일이 하나도 먹지 않는다.
import "./style.css";

import { useEffect, useRef, useState } from "react";

// 지금 어느 모드로 도는지 (TEST / PROD)
import { APP_MODE } from "./config.js";

// 서버와 대화하는 함수 — book_ecma 의 파일을 그대로 쓴다.
import {
    fetchBooks,
    fetchBook,
    createBook,
    updateBook,
    deleteBook,
} from "./api/bookApi.js";

// 입력값 검사 — 이것도 book_ecma 의 파일 그대로다.
import { validateBook } from "./lib/validation.js";

// 폼 값과 서버 데이터 사이의 변환
import { EMPTY_FORM, toRequest, toFormValues } from "./lib/bookData.js";

// 화면 조각
import BookForm from "./components/BookForm.jsx";
import BookTable from "./components/BookTable.jsx";
import BookDetail from "./components/BookDetail.jsx";

// 성공 메시지가 저절로 사라지기까지의 시간(ms) — book_ecma 와 같다.
const MESSAGE_TIMEOUT = 3000;

// 컴포넌트 이름은 대문자로 시작한다. 소문자로 쓰면 React 가 HTML 태그로 본다.
function App() {
    /* ── 2. state 일곱 개 ───────────────────────────────── */

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

    /* ── 3. ref 하나 ────────────────────────────────────── */

    /* useRef 는 화면에 그려진 실제 요소를 붙잡아 두는 자리다.
       state 와 달리 값이 바뀌어도 화면을 다시 그리지 않는다.
       수정 버튼을 눌렀을 때 폼으로 스크롤하는 데만 쓴다. */
    const formRef = useRef(null);

    // 수정 모드인지는 editingId 로 알 수 있으므로 따로 state 를 두지 않는다.
    const isEditing = editingId !== null;

    /* ── 4. 목록 불러오기 ───────────────────────────────── */

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

    /* ── 5. 처음 한 번 ──────────────────────────────────── */

    /* 처음 한 번만 목록을 불러온다.
       book_ecma 의 main.js 맨 아래에 적었던 loadBooks() 한 줄에 해당한다.
       두 번째 인자 [] 가 "처음 한 번만" 이라는 뜻이다.
       빠뜨리면 화면을 그릴 때마다 실행되어 서버 요청이 끝없이 반복된다. */
    useEffect(() => {
        // 아래 주석은 ESLint 에게 "이 경고는 알고 있다" 고 알려 주는 줄이다.
        // eslint-disable-next-line react-hooks/set-state-in-effect -- 처음 한 번 목록을 불러오는 것은 의도된 동작입니다
        loadBooks();
    }, []);

    /* ── 6. 메시지 지우기 ───────────────────────────────── */

    /* 성공 메시지는 3초 뒤에 저절로 사라진다.
       book_ecma 에서 messageTimer 변수를 두고 clearTimeout 을 부르던 일을
       useEffect 가 대신한다. return 으로 돌려준 함수를 정리 함수라고 하는데,
       message 가 바뀌기 직전에 React 가 이것을 먼저 불러 준다.
       그래서 이전 예약이 새 메시지를 지워 버리는 일이 없다. */
    useEffect(() => {
        if (!message) {
            return;
        }

        // 오류 메시지는 사용자가 고칠 때까지 남겨 둔다.
        if (message.type !== "success") {
            return;
        }

        const timer = setTimeout(() => setMessage(null), MESSAGE_TIMEOUT);

        // 정리 함수 — 다음 번 실행 직전과 화면에서 사라질 때 불린다.
        return () => clearTimeout(timer);
    }, [message]);

    /* ── 7. 입력 처리 ───────────────────────────────────── */

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

    // 폼을 비우고 등록 모드로 되돌린다 — book_ecma 의 resetForm()
    // 등록 성공 뒤, 취소 버튼, 수정 중이던 도서를 삭제했을 때 부른다.
    function resetForm() {
        setForm(EMPTY_FORM);
        setEditingId(null);
    }

    /* ── 8. 폼 제출 ─────────────────────────────────────── */

    /* 등록 / 수정 — 폼 제출
       book_ecma 의 submit 핸들러를 그대로 옮겼다.
       달라진 것은 값을 DOM 이 아니라 state 에서 꺼낸다는 점이다. */
    async function handleSubmit(event) {
        event.preventDefault();          // 폼 제출로 페이지가 새로고침되는 것을 막는다
        setMessage(null);                // 앞선 메시지를 지운다 — clearMessages()

        const bookData = toRequest(form);

        // validateBook 은 문제가 있으면 문구를, 없으면 null 을 돌려준다.
        // 검사 함수는 한 줄도 고치지 않았다.
        const errorMessage = validateBook(bookData);
        if (errorMessage) {
            setMessage({ text: errorMessage, type: "error" });
            return;
        }

        try {
            // editingId 에 값이 있으면 수정, 없으면 등록이다.
            if (isEditing) {
                await updateBook(editingId, bookData);
                setMessage({ text: "도서 정보가 성공적으로 수정되었습니다.", type: "success" });
            } else {
                await createBook(bookData);
                setMessage({ text: "도서가 성공적으로 등록되었습니다.", type: "success" });
            }

            resetForm();
            await loadBooks();            // 목록 새로고침
        } catch (error) {
            console.error("Error:", error);
            setMessage({ text: error.message, type: "error" });   // 서버가 보낸 실제 메시지
        }
    }

    /* ── 9. 표의 알림 처리 ──────────────────────────────── */

    /* 수정할 도서를 불러와 폼에 채운다 — book_ecma 의 startEdit()
       tbody 에 걸던 이벤트 위임은 통째로 사라졌다.
       BookTable 의 버튼이 onEdit(book.id) 로 직접 알려 주기 때문이다.
       id 도 props 로 숫자 그대로 오므로 Number() 로 바꿀 필요가 없다. */
    async function handleEdit(bookId) {
        setMessage(null);

        try {
            const book = await fetchBook(bookId);

            // book_ecma 에서는 fillForm 이 input.value 에 하나씩 넣었다.
            // 여기서는 state 만 바꾸면 입력칸이 따라서 바뀐다.
            setForm(toFormValues(book));
            setEditingId(bookId);         // 이제 제출하면 등록이 아니라 수정이 된다

            // formRef.current 는 화면에 그려진 form-container 요소다.
            // 아직 안 그려졌을 수도 있으므로 먼저 확인한다.
            if (formRef.current) {
                formRef.current.scrollIntoView({ behavior: "smooth" });
            }
        } catch (error) {
            console.error("Error:", error);
            setMessage({ text: error.message, type: "error" });
        }
    }

    // 확인을 받은 뒤 도서를 삭제한다 — book_ecma 의 removeBook()
    async function handleDelete(bookId) {
        // 되돌릴 수 없는 일을 하기 전에 잠시 멈춰 세우는 confirm 은 그대로 둔다.
        if (!confirm("정말로 이 도서를 삭제하시겠습니까?")) {
            return;
        }

        try {
            await deleteBook(bookId);
            setMessage({ text: "도서가 성공적으로 삭제되었습니다.", type: "success" });

            // 수정 중이던 도서를 삭제했다면 폼도 등록 모드로 되돌린다.
            // 이걸 빠뜨리면 없는 도서를 수정하려다 404 가 난다.
            if (editingId === bookId) {
                resetForm();
            }

            // 상세 보기로 열어 둔 도서를 삭제했다면 상세 보기도 닫는다.
            if (detailBook?.id === bookId) {
                setDetailBook(null);
            }

            await loadBooks();
        } catch (error) {
            console.error("Error:", error);
            setMessage({ text: error.message, type: "error" });
        }
    }

    /* 도서 한 권의 상세 정보를 보여 준다 — book_ecma 의 showDetail()
       book_ecma 에서는 서버를 부르고 곧바로 alert 을 띄워 남는 값이 없었다.
       여기서는 "지금 무엇을 보여 주는 중인가" 를 detailBook 에 담아 둔다.
       값이 있으면 BookDetail 이 그려지고, null 이면 사라진다. */
    async function handleDetail(bookId) {
        setMessage(null);

        try {
            const book = await fetchBook(bookId);
            setDetailBook(book);
        } catch (error) {
            console.error("Error:", error);
            setMessage({ text: error.message, type: "error" });
        }
    }

    /* ── 10. 배지 계산 ──────────────────────────────────── */

    // 제목 옆에 붙일 배지의 class. 운영이면 빨강, 아니면 회색.
    // "app-mode prod" 처럼 공백으로 띄어 쓰면 클래스를 둘 가진 요소가 된다.
    let modeClass = "app-mode test";
    if (APP_MODE === "PROD") {
        modeClass = "app-mode prod";
    }

    /* ── 11. return ─────────────────────────────────────── */

    // 태그 여러 개를 나란히 돌려줄 수 없으므로 프래그먼트(<> </>)로 감싼다.
    // on 으로 시작하는 props 는 "이런 일이 나면 불러 달라" 는 뜻이고,
    // handle 로 시작하는 함수가 그 일을 실제로 처리한다.
    return (
        <>
            <h1>도서 관리 시스템 <span className={modeClass}>{APP_MODE}</span></h1>

            <BookForm
                form={form}
                isEditing={isEditing}
                message={message}
                onChange={handleChange}
                onSubmit={handleSubmit}
                onCancel={resetForm}
                containerRef={formRef}
            />

            <BookTable
                books={books}
                loading={loading}
                error={listError}
                onEdit={handleEdit}
                onDelete={handleDelete}
                onDetail={handleDetail}
            />

            <BookDetail book={detailBook} onClose={() => setDetailBook(null)} />
        </>
    );
}

// 다른 파일(main.jsx)에서 이름 없이 가져다 쓸 수 있게 내보낸다.
export default App;
