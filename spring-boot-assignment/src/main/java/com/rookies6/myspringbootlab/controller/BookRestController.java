package com.rookies6.myspringbootlab.controller;

import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookRestController {
    private final BookRepository bookRepository;

    //새 도서 등록
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED); //201
    }

    //모든 도서 조회
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    //ID로 특정 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        //Optional 의 map() / orElse() 사용
        return optionalBook.map(book -> ResponseEntity.ok(book)) //Book 이 있는 경우 200
                .orElse(ResponseEntity.notFound().build()); //Book 이 없는 경우 404
    }

    //ISBN으로 도서 조회
    @GetMapping({"/isbn/{isbn}", "/isbn/{isbn}/"})
    public Book getBookByIsbn(@PathVariable String isbn) {
        //BusinessException 과 ErrorObject / DefaultExceptionAdvice 사용
        return getExistBook(bookRepository.findByIsbn(isbn));
    }

    //저자명으로 도서 목록 조회
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }

    //도서 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        Book existBook = getExistBook(bookRepository.findById(id));
        //setter method 호출
        existBook.setTitle(bookDetail.getTitle());
        existBook.setAuthor(bookDetail.getAuthor());
        existBook.setPrice(bookDetail.getPrice());
        existBook.setPublishDate(bookDetail.getPublishDate());
        Book updatedBook = bookRepository.save(existBook);
        return ResponseEntity.ok(updatedBook);
    }

    //도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Book existBook = getExistBook(bookRepository.findById(id));
        bookRepository.delete(existBook);
        return ResponseEntity.noContent().build(); //204
    }

    private Book getExistBook(Optional<Book> optionalBook) {
        return optionalBook
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }
}
