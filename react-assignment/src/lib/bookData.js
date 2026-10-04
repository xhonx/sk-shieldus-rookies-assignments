/* ---------------------------------------------------------
   폼 값과 서버 데이터 사이의 변환
   book_ecma 의 ui/bookForm.js 에서 document 를 쓰지 않는 부분만
   골라 옮겼습니다. React 에서는 입력값이 DOM 이 아니라
   state 에 있기 때문입니다.

     collectBookData()      →  toRequest(form)
     fillForm(book)         →  toFormValues(book)
     toNumberOrNull(value)  →  그대로
   --------------------------------------------------------- */

// 등록 모드의 빈 폼. 폼을 되돌릴 때도 이 값을 쓴다.
// 입력칸 11개 모두 빈 문자열로 시작해야 한다.
// undefined 로 시작하면 React 가 "제어 컴포넌트가 아니게 되었다" 고 경고한다.
export const EMPTY_FORM = {
    title: "",
    author: "",
    isbn: "",
    price: "",
    publishDate: "",
    language: "",
    pageCount: "",
    publisher: "",
    edition: "",
    coverImageUrl: "",
    description: "",
};

/* 숫자 칸(가격, 페이지 수)의 값을 숫자로 바꾼다.
   입력칸의 값은 언제나 문자열이라 "1500" 처럼 들어온다.
   비워 두면 "" 이 오는데, 그대로 보내면 서버가 숫자로 읽지 못한다.
   그래서 비어 있으면 null 을 돌려준다. */
function toNumberOrNull(value) {
    // trim() 으로 공백만 든 칸도 빈 칸으로 본다.
    if (!value || !value.trim()) return null;
    return Number(value);
}

// 폼 state 를 서버가 받는 구조로 바꾼다.
// book_ecma 에서 FormData 로 하던 일인데, 이제 값이 form 객체에 이미 있다.
export function toRequest(form) {
    // 서버는 도서 기본 정보와 상세 정보를 나눠서 받는다.
    return {
        title: form.title.trim(),
        author: form.author.trim(),
        isbn: form.isbn.trim(),
        price: toNumberOrNull(form.price),
        // 여기서는 ?? 가 아니라 || 를 쓴다.
        // ?? 는 빈 문자열("")을 그대로 통과시켜 서버로 "" 이 나가 버린다.
        publishDate: form.publishDate || null,
        bookDetail: {
            description: form.description.trim(),
            language: form.language.trim(),
            pageCount: toNumberOrNull(form.pageCount),
            publisher: form.publisher.trim(),
            coverImageUrl: form.coverImageUrl.trim(),
            edition: form.edition.trim(),
        },
    };
}

// 서버에서 받은 도서 정보를 폼 state 모양으로 바꾼다.
// 수정 버튼을 눌렀을 때 쓴다. DOM 에 넣지 않고 값만 돌려준다.
export function toFormValues(book) {
    // 상세 정보를 등록하지 않은 도서는 bookDetail 이 없다.
    const bookDetail = book.bookDetail;

    // input 의 value 에 undefined 나 null 을 넣으면 React 가 경고를 낸다.
    // 그래서 값이 없을 때는 반드시 빈 문자열로 바꿔 준다.
    //   bookDetail?.language  bookDetail 이 없으면 거기서 멈추고 undefined
    //   ?? ""                 그 undefined 를 빈 문자열로 바꾼다
    return {
        title: book.title ?? "",
        author: book.author ?? "",
        isbn: book.isbn ?? "",
        // 숫자 칸도 state 에는 문자열로 둔다. 입력칸이 돌려주는 값과 같은 모양이다.
        price: String(book.price ?? ""),
        publishDate: book.publishDate ?? "",
        language: bookDetail?.language ?? "",
        pageCount: String(bookDetail?.pageCount ?? ""),
        publisher: bookDetail?.publisher ?? "",
        edition: bookDetail?.edition ?? "",
        coverImageUrl: bookDetail?.coverImageUrl ?? "",
        description: bookDetail?.description ?? "",
    };
}
