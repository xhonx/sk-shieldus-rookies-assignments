/* ---------------------------------------------------------
   메시지 한 줄 — book_ecma 의 ui/message.js 를 다시 썼다
   book_ecma 에서는 showError() 가 formError 요소를 찾아
   textContent 와 style 을 직접 바꿨습니다.

   React 에서는 "무엇을 보여줄지" 만 넘겨받아 그립니다.
   보여줄 것이 없으면 아무것도 그리지 않으므로
   화면을 지우는 clearMessages() 가 따로 필요 없습니다.
   --------------------------------------------------------- */

// 메시지 종류별 글자색 — ui/message.js 의 COLORS 를 그대로 옮겼다.
const COLORS = {
    error: "#f44336",
    success: "#4CAF50",
};

/* 부모가 넘겨주는 값
     message  { text, type } 또는 null
              type 은 "success" 또는 "error" 이고 글자색을 고르는 데만 쓴다 */
function MessageBox({ message }) {
    // null 을 돌려주면 아무것도 그리지 않는다.
    // book_ecma 처럼 display: none 으로 숨길 필요가 없다.
    if (!message) {
        return null;
    }

    // style 에는 문자열이 아니라 객체를 넘긴다.
    const textStyle = { color: COLORS[message.type] ?? COLORS.error };

    return (
        <span className="error-message" style={textStyle}>
            {message.text}
        </span>
    );
}

export default MessageBox;
