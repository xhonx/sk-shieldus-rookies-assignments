# Java 제출연습문제

## 구성

| 폴더 | 내용 | 패키지 |
|---|---|---|
| `01_student_encapsulation/` | 캡슐화 - Student 클래스 | mylab.student.* |
| `02_library_system/` | 도서관 관리 시스템 | mylab.library.* |
| `03_bank_account/` | 은행 계좌 관리 (상속, 예외처리) | mylab.bank.* |
| `04_publication_polymorphism/` | 출판물 관리 & 쇼핑카트 (다형성) | mylab.book.* |

## 컴파일 및 실행 방법

각 폴더 안에서 (Maven 없이 순수 javac 사용):

```bash
# 컴파일
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt

# 실행 (예: 실습1)
java -Dfile.encoding=UTF-8 -cp out mylab.student.control.StudentTest
```

### 실습별 실행 클래스
- 실습1: `mylab.student.control.StudentTest`
- 실습2: `mylab.library.control.LibraryManagementSystem`
- 실습3: `mylab.bank.control.BankDemo`
- 실습4: `mylab.book.control.ManageBook`, `mylab.book.control.ShoppingCart`
