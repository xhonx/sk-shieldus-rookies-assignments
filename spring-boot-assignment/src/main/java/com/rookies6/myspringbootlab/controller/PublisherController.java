package com.rookies6.myspringbootlab.controller;

import com.rookies6.myspringbootlab.controller.dto.BookDTO;
import com.rookies6.myspringbootlab.controller.dto.PublisherDTO;
import com.rookies6.myspringbootlab.service.BookService;
import com.rookies6.myspringbootlab.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;
    private final BookService bookService;

    //모든 출판사 조회
    @GetMapping
    public ResponseEntity<List<PublisherDTO.SimpleResponse>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    //특정 출판사 조회
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    //출판사 이름으로 조회
    @GetMapping("/name/{name}")
    public ResponseEntity<PublisherDTO.Response> getPublisherByName(@PathVariable String name) {
        return ResponseEntity.ok(publisherService.getPublisherByName(name));
    }

    //출판사별 도서 목록 조회
    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO.Response>> getBooksByPublisherId(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBooksByPublisherId(id));
    }

    //새 출판사 생성
    @PostMapping
    public ResponseEntity<PublisherDTO.Response> createPublisher(@Valid @RequestBody PublisherDTO.Request request) {
        PublisherDTO.Response createdPublisher = publisherService.createPublisher(request);
        return new ResponseEntity<>(createdPublisher, HttpStatus.CREATED); //201
    }

    //출판사 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO.Response> updatePublisher(@PathVariable Long id,
                                                                 @Valid @RequestBody PublisherDTO.Request request) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, request));
    }

    //출판사 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build(); //204
    }
}
