/* ---------------------------------------------------------
   도서 목록 표 — book_ecma 의 ui/bookTable.js 를 다시 썼다
   사라진 것들입니다.

     createElement / appendChild   →  JSX 로 태그를 그대로 적는다
     tbody.innerHTML = ""          →  books 가 바뀌면 React 가 다시 그린다
     addCell / createActionButton  →  필요 없다
     버튼에 심던 동작 표시와 위임  →  onClick 에 함수를 직접 넘긴다

   이 컴포넌트는 표를 그리기만 한다. 서버를 부르지도 않고
   값을 저장하지도 않는다. 버튼이 눌리면 부모에게 알릴 뿐이다.
   --------------------------------------------------------- */

// 표의 열 개수. colSpan 에 쓴다.
// 바뀌지 않는 값과 함수는 컴포넌트 바깥에 둔다.
const COLUMN_COUNT = 7;

/* 가격을 "₩12,000" 모양으로 바꾼다. 값이 없으면 "-" 를 돌려준다.
   == null 은 null 과 undefined 를 한 번에 거른다.
   0 원인 책은 "₩0" 으로 제대로 나온다. */
function formatPrice(price) {
    if (price == null) return "-";
    return `₩${price.toLocaleString()}`;
}

/* 부모(App)가 넘겨주는 값들
     books     도서 배열
     loading   불러오는 중인가
     error     목록을 못 불러왔을 때의 문구 (없으면 null)
     onEdit    수정 버튼을 눌렀을 때 부를 함수
     onDelete  삭제 버튼을 눌렀을 때 부를 함수
     onDetail  상세 버튼을 눌렀을 때 부를 함수 */
function BookTable({ books, loading, error, onEdit, onDelete, onDetail }) {
    /* tbody 안에 무엇을 그릴지 세 경우로 나눠서 정한다.
       먼저 rows 에 담아 두고 아래 표 안에 끼워 넣는다. */
    let rows;

    if (error) {
        // (1) 목록을 못 불러왔다 — 표 자리에 그 문구만 그린다.
        rows = (
            <tr>
                <td colSpan={COLUMN_COUNT} className="error-row">{error}</td>
            </tr>
        );
    } else if (books.length === 0 && !loading) {
        // (2) 목록이 비었다. 불러오는 중일 때는 안내를 내지 않는다.
        rows = (
            <tr>
                <td colSpan={COLUMN_COUNT} className="empty-row">등록된 도서가 없습니다.</td>
            </tr>
        );
    } else {
        // (3) 도서 한 권을 행 하나로 그린다.
        //     forEach 는 돌려주는 것이 없고, map 은 새 배열을 돌려준다.
        rows = books.map((book) => (
            // key 는 React 가 어느 행이 어느 행인지 알아보는 표시다.
            // 배열의 순서(index)가 아니라 서버가 준 id 를 쓴다.
            <tr key={book.id}>
                <td>{book.title}</td>
                <td>{book.author}</td>
                <td>{book.isbn}</td>
                <td>{formatPrice(book.price)}</td>
                <td>{book.publishDate ?? "-"}</td>
                {/* 상세 정보가 없는 도서는 bookDetail 이 null 이다. */}
                <td>{book.bookDetail?.publisher ?? "-"}</td>
                <td>
                    {/* onClick 에는 함수를 "넘겨야" 한다. onEdit(book.id) 라고 쓰면
                        그리는 순간 바로 실행되므로 () => 로 감싼다. */}
                    <button type="button" className="edit-btn"
                            onClick={() => onEdit(book.id)}>수정</button>
                    <button type="button" className="delete-btn"
                            onClick={() => onDelete(book.id)}>삭제</button>
                    <button type="button" className="detail-btn"
                            onClick={() => onDetail(book.id)}>상세</button>
                </td>
            </tr>
        ));
    }

    return (
        <div className="table-container">
            <h2>도서 목록</h2>

            {/* style.display 를 켜고 끄는 대신, 참일 때만 그린다. */}
            {loading && <div className="loading">로딩 중...</div>}

            <table>
                <thead>
                    <tr>
                        <th>제목</th>
                        <th>저자</th>
                        <th>ISBN</th>
                        <th>가격</th>
                        <th>출판일</th>
                        <th>출판사</th>
                        <th>액션</th>
                    </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>
        </div>
    );
}

export default BookTable;
