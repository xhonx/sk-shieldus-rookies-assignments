package com.rookies6.myspringbootlab.repository;

import com.rookies6.myspringbootlab.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    //Containing => like %author%
    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByTitleContainingIgnoreCase(String title);

    //Book 과 BookDetail 을 함께 로드
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookDetail WHERE b.id = :id")
    Optional<Book> findByIdWithBookDetail(@Param("id") Long id);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookDetail WHERE b.isbn = :isbn")
    Optional<Book> findByIsbnWithBookDetail(@Param("isbn") String isbn);

    //중복 ISBN 확인
    boolean existsByIsbn(String isbn);

    //Publisher 관련 새로 추가된 메서드

    //특정 출판사의 모든 도서를 조회
    List<Book> findByPublisherId(Long publisherId);

    //특정 출판사의 도서 수를 계산
    @Query("SELECT COUNT(b) FROM Book b WHERE b.publisher.id = :publisherId")
    Long countByPublisherId(@Param("publisherId") Long publisherId);

    //ID로 도서를 조회하면서 BookDetail 과 Publisher 를 모두 즉시 로딩
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.bookDetail LEFT JOIN FETCH b.publisher WHERE b.id = :id")
    Optional<Book> findByIdWithAllDetails(@Param("id") Long id);
}
