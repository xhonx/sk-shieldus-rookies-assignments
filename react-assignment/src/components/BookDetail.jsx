/* ---------------------------------------------------------
   도서 상세 보기 — book_ecma 의 ui/bookDetail.js 를 다시 썼다
   book_ecma 에서는 formatBookDetail 이 여러 줄짜리 문자열을 만들고
   main.js 가 그것을 alert 으로 띄웠습니다.

   React 에서 화면에 낼 것은 문자열이 아니라 JSX 입니다.
   그래서 줄바꿈으로 잇던 줄들이 그대로 화면의 줄이 됩니다.
   alert 과 달리 화면을 멈춰 세우지 않고, style.css 로 꾸밀 수 있고,
   표지 이미지는 주소 글자 대신 그림으로 보여 줍니다.

   보이고 숨기는 것은 book 이 null 인지 아닌지 하나로 정해집니다.
   --------------------------------------------------------- */

// 값이 없을 때 대신 보여 줄 글자. 여러 군데에서 쓰므로 한곳에 둔다.
const EMPTY = "-";

// 가격을 "₩12,000" 모양으로 바꾼다. BookTable 의 것과 같은 규칙이다.
function formatPrice(price) {
    // == 는 null 과 undefined 를 한 번에 거른다. 0 원은 "₩0" 으로 남는다.
    if (price == null) return EMPTY;
    return `₩${price.toLocaleString()}`;
}

/* 부모(App)가 넘겨주는 값들
     book     도서 객체 또는 null
     onClose  닫기 버튼을 누르면 부를 함수 */
function BookDetail({ book, onClose }) {
    // 고른 도서가 없으면 아무것도 그리지 않는다.
    // book.title 을 읽기 전에 먼저 돌려보내야 한다.
    if (!book) {
        return null;
    }

    // 객체를 통째로 중괄호에 넣을 수 없으므로 값을 하나씩 꺼낸다.
    const { title, author, isbn, price, publishDate, bookDetail } = book;

    return (
        <div className="detail-container">
            <h2>도서 상세 정보</h2>

            <dl className="detail-list">
                <dt>제목</dt>
                <dd>{title}</dd>

                <dt>저자</dt>
                <dd>{author}</dd>

                <dt>ISBN</dt>
                <dd>{isbn}</dd>

                <dt>가격</dt>
                <dd>{formatPrice(price)}</dd>

                <dt>출판일</dt>
                <dd>{publishDate ?? EMPTY}</dd>

                {/* 상세 정보가 없는 도서는 bookDetail 이 null 이다.
                    그때는 기본 정보만 보여 준다.
                    글자 칸은 ?? 가 아니라 || 를 쓴다. 입력하지 않은 칸은
                    빈 문자열("")로 저장되는데, ?? 는 그것을 그대로 통과시킨다. */}
                {bookDetail && (
                    <>
                        <dt>설명</dt>
                        <dd>{bookDetail.description || EMPTY}</dd>

                        <dt>언어</dt>
                        <dd>{bookDetail.language || EMPTY}</dd>

                        <dt>페이지 수</dt>
                        <dd>{bookDetail.pageCount ?? EMPTY}</dd>

                        <dt>출판사</dt>
                        <dd>{bookDetail.publisher || EMPTY}</dd>

                        <dt>에디션</dt>
                        <dd>{bookDetail.edition || EMPTY}</dd>

                        <dt>표지 이미지</dt>
                        <dd>
                            {bookDetail.coverImageUrl ? (
                                <img
                                    className="detail-cover"
                                    src={bookDetail.coverImageUrl}
                                    alt={`${title} 표지`}
                                />
                            ) : (
                                EMPTY
                            )}
                        </dd>
                    </>
                )}
            </dl>

            <div className="button-group">
                <button type="button" className="cancel-btn" onClick={onClose}>
                    닫기
                </button>
            </div>
        </div>
    );
}

export default BookDetail;
