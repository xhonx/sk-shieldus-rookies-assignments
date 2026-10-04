package com.rookies6.myspringbootlab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter @Setter
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
}
