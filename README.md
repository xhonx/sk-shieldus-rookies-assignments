# SK Shieldus Rookies 과제 모음

SK쉴더스 루키즈 교육과정에서 진행한 과제들을 주제별로 정리한 레포지토리입니다.

## 📁 디렉토리 구조

| 디렉토리                                          | 내용                                |
| ------------------------------------------------- | ----------------------------------- |
| [`python-data-analysis/`](./python-data-analysis) | 데이터 분석을 위한 Python 학습 과제 |
| [`java-framework/`](./java-framework)             | Spring Framework 학습 과제          |
| [`spring-boot-assignment/`](./spring-boot-assignment)                   | Spring Boot 제출 연습문제 (Book)    |

---

## python-data-analysis

데이터 수집, 전처리, 분석 실습 (Jupyter Notebook)

- `01_news_scraping.ipynb` - 뉴스 스크래핑 실습
- `04_pop_analysis.ipynb` - 인구 데이터 분석 실습

## java-framework

Spring Framework 핵심 개념 실습

- [`spring_di_assignment/`](./java-framework/spring_di_assignment) - Spring DI(Dependency Injection) 전략 3가지 비교 실습
  - 실습1: XML 기반 설정 (Setter / Constructor Injection)
  - 실습2: 어노테이션 + XML 혼합 설정 (`@Component`, `@Autowired`, `component-scan`)
  - 실습3: Java Config 방식 (`@Configuration`, `@Bean`)

## spring-boot-assignment

Spring Boot 제출 연습문제 (도서 관리 시스템) - 제출 단계별로 브랜치를 나누어 진행

- [`spring-boot-assignment/`](./spring-boot-assignment) - Spring Boot 4.0.8 / Java 17 / Maven 프로젝트 (`com.rookies6.myspringbootlab`)
  - 제출2-0 (`main` 브랜치): Spring Boot 프로젝트 생성, 배너 변경, 환경변수 / 프로파일 설정
  - 제출2-1 (`lab_2-1` 브랜치): Book Entity + BookRepository + BookRepositoryTest 작성
  - 제출2-2 (`lab_2-2` 브랜치): BookRestController 작성 (도서 등록 / 조회 / 수정 / 삭제 REST API)
