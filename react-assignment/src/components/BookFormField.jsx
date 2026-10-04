/* ---------------------------------------------------------
   도서 등록 · 수정 폼 — 리팩토링한 것 (과제 13)

   BookForm.jsx 와 하는 일이 똑같습니다. 화면도 동작도 같습니다.
   달라진 것은 코드의 모양뿐입니다.

     BookForm.jsx        입력칸 11개를 하나씩 펼쳐 적었다
     BookFormField.jsx   같은 모양의 input 열 개를 Field 하나로 묶었다

   두 파일 중 하나만 씁니다. App.jsx 의 import 한 줄을 바꿔 가며
   어느 쪽이 읽기 좋은지 견주어 볼 수 있습니다.

   [설명(textarea)을 어떻게 했나]
   textarea 는 묶지 않고 그대로 두었습니다. 태그도 다르고(rows 가 있고
   type 이 없다) 그리드 바깥에 하나뿐이라, Field 에 조건을 하나 더 넣는
   것보다 한 번 펼쳐 적는 편이 Field 를 단순하게 유지하기 때문입니다.
   --------------------------------------------------------- */

import MessageBox from "./MessageBox.jsx";

/* 입력칸 한 개를 그리는 작은 컴포넌트.
   열 칸에서 달라지는 것은 아래 다섯 가지뿐이라, 그것만 밖에서 받는다.

     name      칸의 이름. id 와 name 속성에 함께 쓴다
     label     라벨에 보일 글자
     type      text · number · date · url
     required  반드시 채워야 하는 칸인가 (제목 · 저자 · ISBN 만 참)
     value     지금 값 — form[name]

   onChange 는 부모가 준 것을 그대로 넘긴다.
   이 컴포넌트도 값을 갖지 않는다. 그리기만 한다. */
function Field({ name, label, type, required, value, onChange }) {
    return (
        <div className="form-group">
            <label htmlFor={name}>{label}</label>
            <input
                id={name}
                name={name}
                type={type}
                required={required}
                value={value}
                onChange={onChange}
            />
        </div>
    );
}

/* 부모(App)가 넘겨주는 값들 — BookForm.jsx 와 똑같다 */
function BookForm({
    form,          // 화면에 보일 입력값 11개
    isEditing,     // 수정 모드인가
    message,       // 폼 아래 보여 줄 메시지 — MessageBox 에 그대로 넘긴다
    onChange,      // 입력칸이 바뀔 때 부를 함수
    onSubmit,      // 제출할 때 부를 함수
    onCancel,      // 취소를 누를 때 부를 함수
    containerRef,  // 수정할 때 이 위치로 스크롤하기 위한 참조
}) {
    let containerClass = "form-container";
    if (isEditing) {
        containerClass = "form-container editing";
    }

    return (
        <div className={containerClass} ref={containerRef}>
            <h2>도서 등록</h2>

            <form onSubmit={onSubmit}>
                {/* 펼쳐 쓸 때 열 줄이던 칸 하나가 두 줄이 됐다.
                    달라지는 것만 적고 나머지는 Field 가 알아서 한다. */}
                <div className="form-grid">
                    <Field name="title" label="제목:" type="text" required
                           value={form.title} onChange={onChange} />
                    <Field name="author" label="저자:" type="text" required
                           value={form.author} onChange={onChange} />
                    <Field name="isbn" label="ISBN:" type="text" required
                           value={form.isbn} onChange={onChange} />

                    {/* 여기부터는 required 를 적지 않는다. 비워 두어도 된다. */}
                    <Field name="price" label="가격:" type="number"
                           value={form.price} onChange={onChange} />
                    <Field name="publishDate" label="출판일:" type="date"
                           value={form.publishDate} onChange={onChange} />
                    <Field name="language" label="언어:" type="text"
                           value={form.language} onChange={onChange} />
                    <Field name="pageCount" label="페이지 수:" type="number"
                           value={form.pageCount} onChange={onChange} />
                    <Field name="publisher" label="출판사:" type="text"
                           value={form.publisher} onChange={onChange} />
                    <Field name="edition" label="에디션:" type="text"
                           value={form.edition} onChange={onChange} />
                    <Field name="coverImageUrl" label="표지 이미지 URL:" type="url"
                           value={form.coverImageUrl} onChange={onChange} />
                </div>

                {/* 설명은 input 이 아니라 textarea 라서 묶지 않고 그대로 둔다. */}
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

                    {isEditing && (
                        <button type="button" className="cancel-btn" onClick={onCancel}>
                            취소
                        </button>
                    )}

                    <MessageBox message={message} />
                </div>
            </form>
        </div>
    );
}

export default BookForm;
