package com.rookies6.myspringbootlab.repository;

import com.rookies6.myspringbootlab.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    //출판사 이름으로 특정 출판사를 조회
    Optional<Publisher> findByName(String name);

    //ID로 출판사를 조회하면서 해당 출판사의 모든 도서를 즉시 로딩 (Fetch Join)
    //LEFT JOIN FETCH 이므로 도서가 없는 출판사도 조회된다
    @Query("SELECT p FROM Publisher p LEFT JOIN FETCH p.books WHERE p.id = :id")
    Optional<Publisher> findByIdWithBooks(@Param("id") Long id);

    //특정 이름의 출판사가 존재하는지 확인
    boolean existsByName(String name);
}
