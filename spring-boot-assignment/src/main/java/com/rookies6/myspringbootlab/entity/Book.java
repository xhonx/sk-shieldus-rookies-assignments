package com.rookies6.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    //Primary Key
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    //유니크한 값을 가져야 함
    @Column(unique = true)
    private String isbn;

    private Integer price;

    private LocalDate publishDate;

    //1:1 관계 - mappedBy 로 Book 이 관계의 주인이 아님을 표시 (외래 키는 BookDetail 이 소유)
    //CascadeType.ALL : Book 을 저장/삭제할 때 BookDetail 도 함께 처리
    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private BookDetail bookDetail;
}
