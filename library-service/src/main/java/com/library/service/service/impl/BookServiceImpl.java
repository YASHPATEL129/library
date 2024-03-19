package com.library.service.service.impl;


import com.library.interfaceProjections.BookByIdProjection;
import com.library.pojo.CurrentSession;
import com.library.pojo.response.BookResponse;
import com.library.repository.BookRepository;
import com.library.service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private CurrentSession currentSession;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public BookResponse getBook(Long id) {
        String userName = currentSession.getUserName();
        BookByIdProjection response = bookRepository.getBookById(id, userName);
        BookResponse bookResponse = new BookResponse()
                .setBookId(response.getBookId())
                .setAuthor(response.getAuthor())
                .setCategory(response.getCategory())
                .setIsbn(response.getIsbn())
                .setTitle(response.getTitle())
                .setCover(response.getCover())
                .setPublisher(response.getPublisher())
                .setPages(response.getPages())
                .setModifiedDate(response.getModifiedDate())
                .setModifiedBy(response.getModifiedBy())
                .setCreatedBy(response.getCreatedBy())
                .setCreatedDate(response.getCreatedDate())
                .setDeletedDate(response.getDeletedDate())
                .setIsPrimeValue(response.getIsPrimeValue())
                .setIsDeleted(response.getIsDeleted())
                .setDescription(response.getDescription())
                .setIsPrime(response.getIsPrime())
                .setFile((response.getIsPrime() || response.getIsPrimeValue() == 1) ? response.getFile() : null);

        return bookResponse;
    }
}
