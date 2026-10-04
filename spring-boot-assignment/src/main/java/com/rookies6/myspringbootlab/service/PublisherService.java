package com.rookies6.myspringbootlab.service;

import com.rookies6.myspringbootlab.controller.dto.PublisherDTO;
import com.rookies6.myspringbootlab.entity.Publisher;
import com.rookies6.myspringbootlab.exception.BusinessException;
import com.rookies6.myspringbootlab.exception.ErrorCode;
import com.rookies6.myspringbootlab.repository.BookRepository;
import com.rookies6.myspringbootlab.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    //모든 출판사를 조회하며, 각 출판사의 도서 수를 포함한다
    //books 컬렉션을 로딩하지 않고 count 쿼리로 도서 수만 구한다
    public List<PublisherDTO.SimpleResponse> getAllPublishers() {
        return publisherRepository.findAll() //List<Publisher>
                .stream() //Stream<Publisher>
                .map(publisher -> {
                    Long bookCount = bookRepository.countByPublisherId(publisher.getId());
                    return PublisherDTO.SimpleResponse.fromEntityWithCount(publisher, bookCount);
                }) //Stream<SimpleResponse>
                .toList(); //List<SimpleResponse>
    }

    //ID로 특정 출판사를 조회하며, 해당 출판사의 모든 도서 정보를 포함한다
    public PublisherDTO.Response getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", id));
        return PublisherDTO.Response.fromEntity(publisher);
    }

    //이름으로 특정 출판사를 조회
    public PublisherDTO.Response getPublisherByName(String name) {
        Publisher publisher = publisherRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "name", name));
        return PublisherDTO.Response.fromEntity(publisher);
    }

    //새로운 출판사를 생성
    @Transactional
    public PublisherDTO.Response createPublisher(PublisherDTO.Request request) {
        //이름 중복 검증
        if (publisherRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE, request.getName());
        }

        Publisher publisher = Publisher.builder()
                .name(request.getName())
                .establishedDate(request.getEstablishedDate())
                .address(request.getAddress())
                .build();

        Publisher savedPublisher = publisherRepository.save(publisher);
        return PublisherDTO.Response.fromEntity(savedPublisher);
    }

    //기존 출판사 정보를 수정
    @Transactional
    public PublisherDTO.Response updatePublisher(Long id, PublisherDTO.Request request) {
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", id));

        //이름을 변경하는 경우에만 다른 출판사가 사용 중인지 중복 검증 (자신 제외)
        if (!publisher.getName().equals(request.getName()) &&
                publisherRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.PUBLISHER_NAME_DUPLICATE, request.getName());
        }

        publisher.setName(request.getName());
        publisher.setEstablishedDate(request.getEstablishedDate());
        publisher.setAddress(request.getAddress());

        Publisher updatedPublisher = publisherRepository.save(publisher);
        return PublisherDTO.Response.fromEntity(updatedPublisher);
    }

    //출판사를 삭제 - 해당 출판사에 도서가 있는 경우 삭제를 거부한다
    @Transactional
    public void deletePublisher(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Publisher", "id", id);
        }

        Long bookCount = bookRepository.countByPublisherId(id);
        if (bookCount > 0) {
            throw new BusinessException(ErrorCode.PUBLISHER_HAS_BOOKS, id, bookCount);
        }

        publisherRepository.deleteById(id);
    }
}
