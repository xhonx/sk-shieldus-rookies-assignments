package com.rookies6.myspringbootlab.service;

import com.rookies6.myspringbootlab.controller.dto.BookDTO;
import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.exception.BusinessException;
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

    //모든 도서 조회
    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll() //List<Book>
                .stream() //Stream<Book>
                .map(BookDTO.BookResponse::from) //Stream<BookResponse>
                .toList(); //List<BookResponse>
    }

    //ID로 특정 도서 조회
    public BookDTO.BookResponse getBookById(Long id) {
        Book book = getExistBook(id);
        return BookDTO.BookResponse.from(book);
    }

    //ISBN으로 도서 조회
    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(book);
    }

    //저자명으로 도서 목록 조회
    public List<BookDTO.BookResponse> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .stream()
                .map(BookDTO.BookResponse::from)
                .toList();
    }

    //도서 등록
    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        //ISBN 중복검사
        bookRepository.findByIsbn(request.getIsbn()) //Optional<Book>
                .ifPresent(book -> {
                    throw new BusinessException("Book with this ISBN already Exist", HttpStatus.CONFLICT);
                });
        Book savedBook = bookRepository.save(request.toEntity());
        return BookDTO.BookResponse.from(savedBook);
    }

    //도서 정보 수정
    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book existBook = getExistBook(id);
        //변경이 필요한 필드만 업데이트
        if (request.getTitle() != null) {
            existBook.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            existBook.setAuthor(request.getAuthor());
        }
        if (request.getPrice() != null) {
            existBook.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            existBook.setPublishDate(request.getPublishDate());
        }
        //dirty checking 으로 update 쿼리가 실행된다
        return BookDTO.BookResponse.from(existBook);
    }

    //도서 삭제
    @Transactional
    public void deleteBook(Long id) {
        Book existBook = getExistBook(id);
        bookRepository.delete(existBook);
    }

    private Book getExistBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }
}
