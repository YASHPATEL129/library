package com.library.service.service;

import com.library.pojo.response.BookResponse;
import org.springframework.stereotype.Service;


public interface BookService {

    BookResponse getBook(Long id);
}
