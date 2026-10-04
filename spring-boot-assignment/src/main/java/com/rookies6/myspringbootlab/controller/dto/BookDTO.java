package com.rookies6.myspringbootlab.controller.dto;

import com.rookies6.myspringbootlab.entity.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class BookDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Book title is required")
        private String title;

        @NotBlank(message = "Author name is required")
        private String author;

        @NotBlank(message = "ISBN is required")
        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)")
        private String isbn;

        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        @PastOrPresent(message = "Publish date cannot be in the future")
        private LocalDate publishDate;

        @NotNull(message = "Publisher ID is required")
        private Long publisherId;

        @Valid
        private BookDetailDTO detailRequest;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailDTO {
        private String description;
        private String language;

        @PositiveOrZero(message = "Page count must be positive or zero")
        private Integer pageCount;

        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer price;
        private LocalDate publishDate;
        private PublisherDTO.SimpleResponse publisher;
        private BookDetailResponse detail;

        public static Response fromEntity(Book book) {
            PublisherDTO.SimpleResponse publisherResponse = book.getPublisher() != null
                    ? PublisherDTO.SimpleResponse.fromEntity(book.getPublisher())
                    : null;

            BookDetailResponse detailResponse = book.getBookDetail() != null
                    ? BookDetailResponse.builder()
                    .id(book.getBookDetail().getId())
                    .description(book.getBookDetail().getDescription())
                    .language(book.getBookDetail().getLanguage())
                    .pageCount(book.getBookDetail().getPageCount())
                    .publisher(book.getBookDetail().getPublisher())
                    .coverImageUrl(book.getBookDetail().getCoverImageUrl())
                    .edition(book.getBookDetail().getEdition())
                    .build()
                    : null;

            return Response.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .price(book.getPrice())
                    .publishDate(book.getPublishDate())
                    .publisher(publisherResponse)
                    .detail(detailResponse)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleResponse {
        private Long id;
        private String title;
        private String author;
        private String isbn;

        public static SimpleResponse fromEntity(Book book) {
            return SimpleResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .isbn(book.getIsbn())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailResponse {
        private Long id;
        private String description;
        private String language;
        private Integer pageCount;
        private String publisher;
        private String coverImageUrl;
        private String edition;
    }

    //Book 부분 수정용 (모든 필드가 Optional) - 제출2-4 선택 과제
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PatchRequest {
        private String title;

        private String author;

        @Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$",
                message = "ISBN must be valid (10 or 13 digits, with or without hyphens)")
        private String isbn;

        @PositiveOrZero(message = "Price must be positive or zero")
        private Integer price;

        @PastOrPresent(message = "Publish date cannot be in the future")
        private LocalDate publishDate;

        @Valid
        private BookDetailPatchRequest detailRequest;
    }

    //BookDetail 부분 수정용 - 제출2-4 선택 과제
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailPatchRequest {
        private String description;
        private String language;

        @PositiveOrZero(message = "Page count must be positive or zero")
        private Integer pageCount;

        private String publisher;
        private String coverImageUrl;
        private String edition;
    }
}
