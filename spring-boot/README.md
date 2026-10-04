# SpringBoot 제출 연습문제 (Book)

SK쉴더스 루키즈 6기 Spring Boot 제출 연습문제 프로젝트입니다.

## 프로젝트 설정

| 항목 | 값 |
| --- | --- |
| Spring Boot | 4.0.8 |
| Build | Maven |
| Group / Artifact | `com.rookies6` / `MySpringBootLabProject` |
| Package name | `com.rookies6.myspringbootlab` |
| Packaging / Configuration | Jar / Properties |
| Java | 17 |

**의존성**: Spring Web, Spring Data JPA, H2 Database, MariaDB Driver, Thymeleaf, Validation, Spring Boot DevTools, Lombok, Spring Boot Actuator, Spring Boot Admin (Client), spring-boot-configuration-processor

## 제출 단계별 브랜치

| 제출 | 내용 | 브랜치 |
| --- | --- | --- |
| 제출2-0 | Spring Boot Project 생성 | `main` |
| 제출2-1 | Entity + Repository + Test케이스 작성 | `lab_2-1` |
| 제출2-2 | Entity + Repository + RestController 작성 | `lab_2-2` |
| 제출2-3 | RestController + Service + Entity와 Repository + `@Valid` + DTO | `lab_2-3` |
| 제출2-4 | 1:1 연관관계 Entity (Book - BookDetail) | `lab_2-4` |

## [제출2-0] Spring Boot 프로젝트 작성하기

| 문항 | 내용 | 구현 위치 |
| --- | --- | --- |
| 1-1 | Spring Initializer 에서 스프링 부트 프로젝트 작성 | `pom.xml` |
| 1-2 | Spring Boot 배너 변경 | `src/main/resources/banner.txt` |
| 1-3 | `application.properties` 에 환경변수 설정 (`myprop.username`, `myprop.port`) | `src/main/resources/application.properties` |
| 1-4 | `@Value` 로 환경변수를 Load 하여 출력하는 `MyPropRunner` 작성 | `runner/MyPropRunner.java` |
| 1-5 | `@Component`, `@ConfigurationProperties` 를 선언한 `MyPropProperties` 작성 후 `MyPropRunner` 에 주입 | `property/MyPropProperties.java` |
| 1-6 | `MyEnvironment` 와 `ProdConfig`(`@Profile("prod")`, 운영환경) / `TestConfig`(`@Profile("test")`, 개발환경) 작성 | `config/` |
| 1-7 | 프로파일용 properties 작성 (prod: INFO, test: DEBUG) | `application-prod.properties`, `application-test.properties` |
| 1-8 | `MyPropRunner` 의 출력을 `logger.debug()` / `logger.info()` 로 변경 | `runner/MyPropRunner.java` |
| 1-9 | jar 파일로 생성하여 실행 | 아래 실행 방법 참고 |

## [제출2-1] Book Entity + Repository + Test케이스 작성

도서 관리 시스템 구현하기 (`lab_2-1` 브랜치)

| 구분 | 내용 | 구현 위치 |
| --- | --- | --- |
| Entity | `Book` : id(Long, PK), title(String), author(String), isbn(String, Unique), publishDate(LocalDate), price(Integer) / `@Table(name = "books")` | `entity/Book.java` |
| Repository | `BookRepository` : `findByIsbn(String isbn)`, `findByAuthor(String author)` | `repository/BookRepository.java` |
| Test | `BookRepositoryTest` (`@DataJpaTest`) | `src/test/.../repository/BookRepositoryTest.java` |

**테스트 케이스**

| 메서드 | 내용 |
| --- | --- |
| `testCreateBook()` | 도서 등록 테스트 |
| `testFindByIsbn()` | ISBN으로 도서 조회 테스트 |
| `testFindByAuthor()` | 저자명으로 도서 목록 조회 테스트 |
| `testUpdateBook()` | 도서 정보 수정 테스트 |
| `testDeleteBook()` | 도서 삭제 테스트 |

**테스트 데이터**

| title | author |
| --- | --- |
| 스프링 부트 입문 | 홍길동 |
| JPA 프로그래밍 | 박둘리 |

**DB 설정**

| 프로파일 | DB | 설정 파일 |
| --- | --- | --- |
| `prod` (기본) | MariaDB `lab_db` (lab 계정) | `application-prod.properties` |
| `test` | H2 (in-memory) | `application-test.properties` |

MariaDB lab 계정 생성

```sql
create database lab_db;
CREATE USER 'lab'@'%' IDENTIFIED BY 'lab';
GRANT ALL PRIVILEGES ON lab_db.* TO 'lab'@'%';
flush privileges;
```

테스트 실행

```bash
./mvnw test -Dtest=BookRepositoryTest
```

## [제출2-2] BookRestController + Entity + Repository

`BookRestController` 작성 (`lab_2-2` 브랜치) - `BusinessException`, `DefaultExceptionAdvice`, `ErrorObject` 클래스 사용

| Method | URL | 내용 | 응답 |
| --- | --- | --- | --- |
| POST | `/api/books` | 새 도서 등록 | 201 Created |
| GET | `/api/books` | 모든 도서 조회 | 200 OK |
| GET | `/api/books/{id}` | ID로 특정 도서 조회 | 200 OK / 404 Not Found |
| GET | `/api/books/isbn/{isbn}/` | ISBN으로 도서 조회 | 200 OK / 404 Not Found |
| GET | `/api/books/author/{author}` | 저자명으로 도서 목록 조회 | 200 OK |
| PUT | `/api/books/{id}` | 도서 정보 수정 | 200 OK / 404 Not Found |
| DELETE | `/api/books/{id}` | 도서 삭제 | 204 No Content / 404 Not Found |

**Book 이 존재하지 않을 때 (404) 처리**

- `getBookById()` : `Optional` 클래스의 `map()` / `orElse()` 를 사용하여 `ResponseEntity<Book>` 반환
- `getBookByIsbn()` : `BusinessException` 과 `ErrorObject` / `DefaultExceptionAdvice` 사용

```json
{
  "message": "Book Not Found",
  "statusCode": 404,
  "timestamp": "2026-10-04 21:52:11 일 오후"
}
```

**새로운 도서 등록 할 때 사용하는 Request Body Json 데이터**

```json
{
  "title": "스프링 부트 입문",
  "author": "홍길동",
  "isbn": "9788956746425",
  "price": 30000,
  "publishDate": "2025-05-07"
}
```

```json
{
  "title": "JPA 프로그래밍",
  "author": "박둘리",
  "isbn": "9788956746432",
  "price": 35000,
  "publishDate": "2025-04-30"
}
```

## [제출2-3] RestController + Service + Entity + Repository

도서 관리 시스템 구현하기 - Service와 DTO 추가하기 (`lab_2-3` 브랜치)

**계층 구조**

| 계층 | 클래스 | 내용 |
| --- | --- | --- |
| Repository | `Book`, `BookRepository` | JPA 엔티티와 데이터 액세스 (ISBN, 저자로 검색) |
| DTO | `BookDTO` (`controller/dto`) | 내부 클래스 `BookCreateRequest`, `BookUpdateRequest`, `BookResponse` / 검증 애노테이션 포함 |
| Service | `BookService` | 비즈니스 로직 담당, `@Transactional` 로 트랜잭션 관리, Entity <=> DTO 변환 (Stream API) |
| Controller | `BookController` | 서비스 계층에 비즈니스 로직을 위임, `@Valid` 로 요청 본문 검증 |
| 예외 처리 | `BusinessException`, `DefaultExceptionAdvice` | 비즈니스 예외와 검증 예외를 구분하여 일관된 형식으로 응답 |

**API**

| Method | URL | 내용 | 응답 |
| --- | --- | --- | --- |
| POST | `/api/books` | 새 도서 등록 (`BookCreateRequest`) | 201 Created / 400 검증 오류 / 409 ISBN 중복 |
| GET | `/api/books` | 모든 도서 조회 | 200 OK |
| GET | `/api/books/{id}` | ID로 특정 도서 조회 | 200 OK / 404 Not Found |
| GET | `/api/books/isbn/{isbn}/` | ISBN으로 도서 조회 | 200 OK / 404 Not Found |
| GET | `/api/books/author/{author}` | 저자명으로 도서 목록 조회 | 200 OK |
| PUT | `/api/books/{id}` | 도서 정보 수정 (`BookUpdateRequest`) | 200 OK / 400 검증 오류 / 404 Not Found |
| DELETE | `/api/books/{id}` | 도서 삭제 | 204 No Content / 404 Not Found |

**수정 (Update)**

- 저자(author), 가격(price), 제목(title), 출판일자(publishDate) 를 수정합니다.
- `BookService` 의 `updateBook()` 메서드에서 입력값이 있는 경우에만 값을 변경합니다.

```json
{
  "price": 32000
}
```

**유효성 검증 오류 응답 (400)**

```json
{
  "status": 400,
  "message": "입력항목 검증 오류",
  "timestamp": "2026-10-04T22:04:38.1958286",
  "errors": {
    "title": "제목은 필수 입력항목입니다.",
    "isbn": "ISBN은 필수 입력항목입니다.",
    "price": "가격은 0보다 커야 합니다.",
    "publishDate": "출판일자는 필수 입력항목입니다."
  }
}
```

**비즈니스 예외 응답 (404 / 409)**

```json
{
  "message": "Book with this ISBN already Exist",
  "statusCode": 409,
  "timestamp": "2026-10-04 22:04:38 일 오후"
}
```

## [제출2-4] Book 과 BookDetail ( 1:1 관계 )

도서 관리 시스템 구현하기 - 1:1 연관관계 (`lab_2-4` 브랜치)

**엔티티**

| 클래스 | 테이블 | 내용 |
| --- | --- | --- |
| `Book` | `books` | 책의 기본 정보 (제목, 저자, ISBN, 가격, 출판일) / `@OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)` 로 `BookDetail` 과 연결 (관계의 주인이 아님) |
| `BookDetail` | `book_details` | 책의 상세 정보 (설명, 언어, 페이지 수, 출판사, 표지 이미지 URL, 에디션) / `@OneToOne(fetch = FetchType.LAZY)`, `@JoinColumn(name = "book_id", unique = true)` (관계의 주인, 외래 키 소유) |

- **1:1 관계** : 각 책은 하나의 상세 정보만 가질 수 있습니다.
- **지연 로딩** : `FetchType.LAZY` 를 사용하여 필요할 때만 연관된 데이터를 로드합니다.
- **영속성 전이** : `CascadeType.ALL` 을 사용하여 `Book` 을 저장/삭제할 때 `BookDetail` 도 함께 처리됩니다.

**레포지토리**

| 인터페이스 | 메서드 |
| --- | --- |
| `BookRepository` | `findByIsbn`, `findByAuthorContainingIgnoreCase`, `findByTitleContainingIgnoreCase`, `findByIdWithBookDetail`, `findByIsbnWithBookDetail`, `existsByIsbn` |
| `BookDetailRepository` | `findByBookId`, `findByIdWithBook`, `findByPublisher` |

**DTO** (`BookDTO`)

| 클래스 | 내용 |
| --- | --- |
| `Request` | 책 생성/수정 시 사용하는 DTO (ISBN 패턴 검사, 가격 음수 방지, 출간일 과거 날짜 제한) |
| `BookDetailDTO` | 책 상세 정보 요청용 중첩 DTO |
| `Response` | API 응답용 DTO |
| `BookDetailResponse` | 책 상세 정보 응답용 중첩 DTO |

**API** (`BookController` -> `BookService`)

| Method | URL | 내용 | 응답 |
| --- | --- | --- | --- |
| GET | `/api/books` | 모든 책 조회 | 200 OK |
| GET | `/api/books/{id}` | ID로 책 조회 | 200 OK / 404 Not Found |
| GET | `/api/books/isbn/{isbn}` | ISBN으로 책 조회 | 200 OK / 404 Not Found |
| GET | `/api/books/search/author?author={author}` | 저자로 책 검색 | 200 OK |
| GET | `/api/books/search/title?title={title}` | 제목으로 책 검색 | 200 OK |
| POST | `/api/books` | 책 생성 | 201 Created / 400 검증 오류 / 409 ISBN 중복 |
| PUT | `/api/books/{id}` | 책 수정 (전체수정) | 200 OK / 400 검증 오류 / 404 Not Found / 409 ISBN 중복 |
| DELETE | `/api/books/{id}` | 책 삭제 | 204 No Content / 404 Not Found |

**등록 (POST) Request Body**

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "price": 45,
  "publishDate": "2008-08-01",
  "detailRequest": {
    "description": "A handbook of agile software craftsmanship",
    "language": "English",
    "pageCount": 464,
    "publisher": "Prentice Hall",
    "coverImageUrl": "https://example.com/cleancode.jpg",
    "edition": "1st"
  }
}
```

**레포지토리 테스트** (`BookRepositoryTest`)

| 메서드 | 내용 |
| --- | --- |
| `createBookWithBookDetail()` | 책과 책 상세 정보 생성 |
| `findBookByIsbn()` | ISBN으로 책 조회 |
| `findByIdWithBookDetail()` | 책과 책 상세 정보를 함께 조회 |
| `findBooksByAuthor()` | 저자로 책 검색 |
| `findBookDetailByBookId()` | 책 ID로 상세 정보 조회 |

## 실행 방법

jar 파일 생성

```bash
./mvnw clean package
```

포트번호를 변경하고 `myprop.username` 환경변수의 값 변경하기

```bash
java -jar -Dserver.port=8083 ./target/MySpringBootLabProject-0.0.1-SNAPSHOT.jar --myprop.username=Springfw
```

로그레벨을 변경하고 `spring.profiles.active` 값을 `prod` 로 설정하기

```bash
java -jar -Dlogging.level.com.rookies6.myspringbootlab=warn ./target/MySpringBootLabProject-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```
