package com.rookies6.myspringbootlab.service;

import com.rookies6.myspringbootlab.controller.dto.BookDTO;
import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.entity.BookDetail;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.repository.BookDetailRepository;
import com.rookies6.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    //모든 책 조회
    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll() //List<Book>
                .stream() //Stream<Book>
                .map(BookDTO.Response::fromEntity) //Stream<Response>
                .toList(); //List<Response>
    }

    //ID로 특정 책 조회
    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    //ISBN으로 특정 책 조회
    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    //저자로 책 검색
    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    //제목으로 책 검색
    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    //책 생성
    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        //ISBN 중복 검사
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("Book with this ISBN already Exist", HttpStatus.CONFLICT);
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        //책 상세 정보가 있는 경우 양방향 연관관계 설정
        BookDTO.BookDetailDTO detailRequest = request.getDetailRequest();
        if (detailRequest != null) {
            BookDetail bookDetail = BookDetail.builder()
                    .description(detailRequest.getDescription())
                    .language(detailRequest.getLanguage())
                    .pageCount(detailRequest.getPageCount())
                    .publisher(detailRequest.getPublisher())
                    .coverImageUrl(detailRequest.getCoverImageUrl())
                    .edition(detailRequest.getEdition())
                    .book(book)
                    .build();
            book.setBookDetail(bookDetail);
        }

        //CascadeType.ALL 이므로 Book 을 저장하면 BookDetail 도 함께 저장된다
        Book savedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(savedBook);
    }

    //책 수정
    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = bookRepository.findByIdWithBookDetail(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));

        //ISBN 을 변경하는 경우에만 다른 책이 사용 중인지 중복 검사
        if (!book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("Book with this ISBN already Exist", HttpStatus.CONFLICT);
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        BookDTO.BookDetailDTO detailRequest = request.getDetailRequest();
        if (detailRequest != null) {
            BookDetail bookDetail = book.getBookDetail();
            //상세 정보가 없던 책이면 새로 생성하여 연결
            if (bookDetail == null) {
                bookDetail = new BookDetail();
                bookDetail.setBook(book);
                book.setBookDetail(bookDetail);
            }
            bookDetail.setDescription(detailRequest.getDescription());
            bookDetail.setLanguage(detailRequest.getLanguage());
            bookDetail.setPageCount(detailRequest.getPageCount());
            bookDetail.setPublisher(detailRequest.getPublisher());
            bookDetail.setCoverImageUrl(detailRequest.getCoverImageUrl());
            bookDetail.setEdition(detailRequest.getEdition());
        }

        Book updatedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(updatedBook);
    }

    //책 삭제
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        //CascadeType.ALL 이므로 BookDetail 도 함께 삭제된다
        bookRepository.delete(book);
    }
}
