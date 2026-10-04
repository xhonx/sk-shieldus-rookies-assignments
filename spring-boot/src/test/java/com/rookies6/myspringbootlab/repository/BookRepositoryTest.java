package com.rookies6.myspringbootlab.repository;

import com.rookies6.myspringbootlab.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    //도서 등록 테스트
    @Test
    void testCreateBook() {
        //Given (준비 단계)
        Book book = createBook("스프링 부트 입문", "홍길동", "9788956746425", 30000, LocalDate.of(2025, 5, 7));
        //When (실행 단계)
        Book savedBook = bookRepository.save(book);
        //Then (검증 단계)
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("스프링 부트 입문");
        assertThat(savedBook.getAuthor()).isEqualTo("홍길동");
    }

    //ISBN으로 도서 조회 테스트
    @Test
    void testFindByIsbn() {
        bookRepository.save(createBook("스프링 부트 입문", "홍길동", "9788956746425", 30000, LocalDate.of(2025, 5, 7)));

        Optional<Book> optionalBook = bookRepository.findByIsbn("9788956746425");

        assertThat(optionalBook).isPresent();
        assertThat(optionalBook.get().getTitle()).isEqualTo("스프링 부트 입문");

        //존재하지 않는 ISBN 으로 조회하는 경우
        assertThat(bookRepository.findByIsbn("0000000000000")).isEmpty();
    }

    //저자명으로 도서 목록 조회 테스트
    @Test
    void testFindByAuthor() {
        bookRepository.save(createBook("스프링 부트 입문", "홍길동", "9788956746425", 30000, LocalDate.of(2025, 5, 7)));
        bookRepository.save(createBook("JPA 프로그래밍", "박둘리", "9788956746432", 35000, LocalDate.of(2025, 4, 30)));

        List<Book> books = bookRepository.findByAuthor("박둘리");

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("JPA 프로그래밍");
    }

    //도서 정보 수정 테스트
    @Test
    void testUpdateBook() {
        Book savedBook = bookRepository.save(
                createBook("스프링 부트 입문", "홍길동", "9788956746425", 30000, LocalDate.of(2025, 5, 7)));

        //수정하려면 Entity 의 setter method 를 호출한다
        Book book = bookRepository.findById(savedBook.getId()).orElseThrow();
        book.setTitle("스프링 부트 입문 개정판");
        book.setPrice(32000);
        bookRepository.saveAndFlush(book);

        Book updatedBook = bookRepository.findById(savedBook.getId()).orElseThrow();
        assertThat(updatedBook.getTitle()).isEqualTo("스프링 부트 입문 개정판");
        assertThat(updatedBook.getPrice()).isEqualTo(32000);
    }

    //도서 삭제 테스트
    @Test
    void testDeleteBook() {
        Book savedBook = bookRepository.save(
                createBook("JPA 프로그래밍", "박둘리", "9788956746432", 35000, LocalDate.of(2025, 4, 30)));

        bookRepository.delete(savedBook);
        bookRepository.flush();

        assertThat(bookRepository.findById(savedBook.getId())).isEmpty();
    }

    private Book createBook(String title, String author, String isbn, Integer price, LocalDate publishDate) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(price);
        book.setPublishDate(publishDate);
        return book;
    }
}
