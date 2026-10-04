package com.rookies6.myspringbootlab.controller.dto;

import com.rookies6.myspringbootlab.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class BookDTO {

    //도서 생성 시 사용되는 DTO
    @Getter
    @Setter
    public static class BookCreateRequest {
        @NotBlank(message = "제목은 필수 입력항목입니다.")
        private String title;

        @NotBlank(message = "저자는 필수 입력항목입니다.")
        private String author;

        @NotBlank(message = "ISBN은 필수 입력항목입니다.")
        private String isbn;

        @NotNull(message = "가격은 필수 입력항목입니다.")
        @Positive(message = "가격은 0보다 커야 합니다.")
        private Integer price;

        @NotNull(message = "출판일자는 필수 입력항목입니다.")
        private LocalDate publishDate;

        public Book toEntity() {
            Book book = new Book();
            book.setTitle(this.title);
            book.setAuthor(this.author);
            book.setIsbn(this.isbn);
            book.setPrice(this.price);
            book.setPublishDate(this.publishDate);
            return book;
        }
    }

    //도서 정보 업데이트 시 사용되는 DTO (입력값이 있는 항목만 수정하므로 null 허용)
    @Getter
    @Setter
    public static class BookUpdateRequest {
        @Positive(message = "가격은 0보다 커야 합니다.")
        private Integer price;

        @Pattern(regexp = ".*\\S.*", message = "제목은 공백일 수 없습니다.")
        private String title;

        @Pattern(regexp = ".*\\S.*", message = "저자는 공백일 수 없습니다.")
        private String author;

        private LocalDate publishDate;
    }

    //클라이언트에게 반환되는 도서 정보 DTO
    @Getter
    @Builder
    public static class BookResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;

        public static BookResponse from(Book book) {
            return BookResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .build();
        }
    }
}
