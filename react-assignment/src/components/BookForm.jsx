/* ---------------------------------------------------------
   도서 등록 · 수정 폼 — book_ecma 의 ui/bookForm.js 를 다시 썼다
   book_ecma 에서는 폼이 index.html 에 있었고, ui/bookForm.js 가
   그 요소를 찾아 값을 읽고 쓰고 버튼 글자를 바꿨습니다.

   React 에서는 폼이 이 파일 안에 있습니다. 그리고 입력칸의
   값은 DOM 이 아니라 부모가 준 form 객체에서 옵니다.

     화면에 보이는 값 = props.form
     값이 바뀌면      = props.onChange 로 부모에게 알린다

   이런 입력을 제어 컴포넌트(controlled component)라고 합니다.
   이 컴포넌트는 값을 저장하지 않습니다. 그리기만 합니다.

   입력칸 11개가 생김새는 거의 같지만 일부러 하나씩 펼쳐 적었습니다.
   위에서 아래로 한 번에 읽히는 것이 지금은 더 중요하기 때문입니다.
   --------------------------------------------------------- */

import MessageBox from "./MessageBox.jsx";

/* 부모(App)가 넘겨주는 값들 */
function BookForm({
    form,          // 화면에 보일 입력값 11개
    isEditing,     // 수정 모드인가
    message,       // 폼 아래 보여 줄 메시지 — MessageBox 에 그대로 넘긴다
    onChange,      // 입력칸이 바뀔 때 부를 함수
    onSubmit,      // 제출할 때 부를 함수
    onCancel,      // 취소를 누를 때 부를 함수
}) {
    // book_ecma 의 setEditMode 가 classList.toggle 로 하던 일을 문자열로 표현한다.
    // 이 클래스는 style.css 에서 폼 왼쪽에 녹색 띠를 그린다.
    let containerClass = "form-container";
    if (isEditing) {
        containerClass = "form-container editing";
    }

    return (
        <div className={containerClass}>
            <h2>도서 등록</h2>

            <form onSubmit={onSubmit}>
                <div className="form-grid">
                    {/* 입력칸 한 개는 언제나 이 세 가지가 짝이다.
                          value    = {form.어느칸}   보이는 값은 부모에게서 온다
                          onChange = {onChange}      바뀌면 부모에게 알린다
                          name     = "어느칸"        부모가 어느 칸인지 알아보는 이름 */}
                    <div className="form-group">
                        <label htmlFor="title">제목:</label>
                        <input
                            id="title"
                            name="title"
                            type="text"
                            required
                            value={form.title}
                            onChange={onChange}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="author">저자:</label>
                        <input
                            id="author"
                            name="author"
                            type="text"
                            required
                            value={form.author}
                            onChange={onChange}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="isbn">ISBN:</label>
                        <input
                            id="isbn"
                            name="isbn"
                            type="text"
                            required
                            value={form.isbn}
                            onChange={onChange}
                        />
                    </div>

                    {/* type 이 number 여도 state 에는 숫자가 아니라 문자열로 들어온다. */}
                    <div className="form-group">
                        <label htmlFor="price">가격:</label>
                        <input
                            id="price"
                            name="price"
                            type="number"
                            value={form.price}
                            onChange={onChange}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="publishDate">출판일:</label>
                        <input
                            id="publishDate"
                            name="publishDate"
                            type="date"
                            value={form.publishDate}
                            onChange={onChange}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="language">언어:</label>
                        <input
                            id="language"
                            name="language"
                            type="text"
                            value={form.language}
                            onChange={onChange}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="pageCount">페이지 수:</label>
                        <input
                            id="pageCount"
                            name="pageCount"
                            type="number"
                            value={form.pageCount}
                            onChange={onChange}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="publisher">출판사:</label>
                        <input
                            id="publisher"
                            name="publisher"
                            type="text"
                            value={form.publisher}
                            onChange={onChange}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="edition">에디션:</label>
                        <input
                            id="edition"
                            name="edition"
                            type="text"
                            value={form.edition}
                            onChange={onChange}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="coverImageUrl">표지 이미지 URL:</label>
                        <input
                            id="coverImageUrl"
                            name="coverImageUrl"
                            type="url"
                            value={form.coverImageUrl}
                            onChange={onChange}
                        />
                    </div>
                </div>

                {/* 설명은 한 줄을 다 쓰도록 그리드 바깥에 둔다.
                    HTML 에서는 여는 태그와 닫는 태그 사이에 글을 넣지만,
                    JSX 에서는 input 과 똑같이 value 와 onChange 로 다룬다. */}
                <div className="form-group">
                    <label htmlFor="description">설명:</label>
                    <textarea
                        id="description"
                        name="description"
                        rows="4"
                        value={form.description}
                        onChange={onChange}
                    />
                </div>

                <div className="button-group">
                    <button type="submit">{isEditing ? "도서 수정" : "도서 등록"}</button>

                    {/* book_ecma 에서는 style.display 를 바꿨지만, 여기서는 아예 그리지 않는다.
                        조건 && 화면 은 "조건이 참일 때만 그린다" 는 뜻이다. */}
                    {isEditing && (
                        <button type="button" className="cancel-btn" onClick={onCancel}>
                            취소
                        </button>
                    )}

                    {/* 메시지가 없으면 MessageBox 가 null 을 돌려주어 아무것도 그려지지 않는다. */}
                    <MessageBox message={message} />
                </div>
            </form>
        </div>
    );
}

export default BookForm;
