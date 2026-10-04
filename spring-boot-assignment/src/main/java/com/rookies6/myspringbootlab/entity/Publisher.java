package com.rookies6.myspringbootlab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "publishers")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {
    //Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate establishedDate;

    private String address;

    //1:N 관계 - mappedBy 로 Publisher 가 관계의 주인이 아님을 표시 (외래 키는 Book 이 소유)
    //@Builder.Default : builder 로 생성할 때도 빈 리스트로 초기화
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private List<Book> books = new ArrayList<>();

    //연관관계 편의 메서드 - 양쪽 객체에 함께 반영한다
    public void addBook(Book book) {
        books.add(book);
        book.setPublisher(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setPublisher(null);
    }
}
