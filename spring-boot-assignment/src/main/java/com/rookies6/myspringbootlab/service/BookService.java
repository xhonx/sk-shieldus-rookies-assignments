package com.rookies6.myspringbootlab.service;

import com.rookies6.myspringbootlab.controller.dto.BookDTO;
import com.rookies6.myspringbootlab.entity.Book;
import com.rookies6.myspringbootlab.entity.BookDetail;
import com.rookies6.myspringbootlab.entity.Publisher;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.exception.ErrorCode;
import com.rookies6.myspringbootlab.repository.BookDetailRepository;
import com.rookies6.myspringbootlab.repository.BookRepository;
import com.rookies6.myspringbootlab.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;
    private final PublisherRepository publisherRepository;

    //모든 도서 조회
    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll() //List<Book>
                .stream() //Stream<Book>
                .map(BookDTO.Response::fromEntity) //Stream<Response>
                .toList(); //List<Response>
    }

    //ID로 특정 도서 조회 - 출판사, 상세정보를 함께 로딩한다
    public BookDTO.Response getBookById(Long id) {
        Book book = bookRepository.findByIdWithAllDetails(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
        return BookDTO.Response.fromEntity(book);
    }

    //ISBN으로 특정 도서 조회
    public BookDTO.Response getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbnWithBookDetail(isbn)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "ISBN", isbn));
        return BookDTO.Response.fromEntity(book);
    }

    //작가명으로 도서 검색
    public List<BookDTO.Response> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    //제목으로 도서 검색
    public List<BookDTO.Response> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    //특정 출판사의 모든 도서 조회
    public List<BookDTO.Response> getBooksByPublisherId(Long publisherId) {
        //출판사 존재 여부 검증
        if (!publisherRepository.existsById(publisherId)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", publisherId);
        }

        return bookRepository.findByPublisherId(publisherId)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    //새로운 도서 생성
    @Transactional
    public BookDTO.Response createBook(BookDTO.Request request) {
        //출판사 존재 여부 검증
        Publisher publisher = getExistPublisher(request.getPublisherId());

        //ISBN 중복 검증
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .publisher(publisher)
                .build();

        //도서 상세 정보가 있는 경우 양방향 연관관계 설정
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

    //기존 도서 정보 수정
    @Transactional
    public BookDTO.Response updateBook(Long id, BookDTO.Request request) {
        Book book = getExistBook(id);

        //출판사 유효성 검증
        Publisher publisher = getExistPublisher(request.getPublisherId());

        //ISBN 을 변경하는 경우에만 다른 도서가 사용 중인지 중복 검증
        if (!book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());
        book.setPublisher(publisher);

        BookDTO.BookDetailDTO detailRequest = request.getDetailRequest();
        if (detailRequest != null) {
            BookDetail bookDetail = book.getBookDetail();
            //상세 정보가 없던 도서이면 새로 생성하여 연결
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

    //도서 부분 수정 - 제공된 필드만 업데이트하고, 제공되지 않은 필드는 기존 값 유지
    @Transactional
    public BookDTO.Response patchBook(Long id, BookDTO.PatchRequest request) {
        Book book = getExistBook(id);

        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }
        if (request.getIsbn() != null) {
            //새로운 ISBN 으로 변경하는데 이미 다른 도서가 사용 중이면 오류
            if (!book.getIsbn().equals(request.getIsbn()) &&
                    bookRepository.existsByIsbn(request.getIsbn())) {
                throw new BusinessException(ErrorCode.ISBN_DUPLICATE, request.getIsbn());
            }
            book.setIsbn(request.getIsbn());
        }
        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }
        if (request.getPublishDate() != null) {
            book.setPublishDate(request.getPublishDate());
        }
        if (request.getDetailRequest() != null) {
            patchBookDetail(book, request.getDetailRequest());
        }

        Book updatedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(updatedBook);
    }

    //도서 상세 정보 부분 수정
    @Transactional
    public BookDTO.Response patchBookDetail(Long id, BookDTO.BookDetailPatchRequest request) {
        Book book = getExistBook(id);

        patchBookDetail(book, request);

        Book updatedBook = bookRepository.save(book);
        return BookDTO.Response.fromEntity(updatedBook);
    }

    //도서 삭제 - CascadeType.ALL 이므로 BookDetail 도 함께 삭제된다
    @Transactional
    public void deleteBook(Long id) {
        Book book = getExistBook(id);
        bookRepository.delete(book);
    }

    private void patchBookDetail(Book book, BookDTO.BookDetailPatchRequest request) {
        BookDetail bookDetail = book.getBookDetail();
        //상세 정보가 없던 도서이면 새로 생성하여 연결
        if (bookDetail == null) {
            bookDetail = new BookDetail();
            bookDetail.setBook(book);
            book.setBookDetail(bookDetail);
        }

        if (request.getDescription() != null) {
            bookDetail.setDescription(request.getDescription());
        }
        if (request.getLanguage() != null) {
            bookDetail.setLanguage(request.getLanguage());
        }
        if (request.getPageCount() != null) {
            bookDetail.setPageCount(request.getPageCount());
        }
        if (request.getPublisher() != null) {
            bookDetail.setPublisher(request.getPublisher());
        }
        if (request.getCoverImageUrl() != null) {
            bookDetail.setCoverImageUrl(request.getCoverImageUrl());
        }
        if (request.getEdition() != null) {
            bookDetail.setEdition(request.getEdition());
        }
    }

    private Book getExistBook(Long id) {
        return bookRepository.findByIdWithAllDetails(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Book", "id", id));
    }

    private Publisher getExistPublisher(Long publisherId) {
        return publisherRepository.findById(publisherId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", publisherId));
    }
}
