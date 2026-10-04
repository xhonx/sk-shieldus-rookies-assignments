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
