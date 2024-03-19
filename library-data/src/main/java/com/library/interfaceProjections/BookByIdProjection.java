package com.library.interfaceProjections;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.library.entity.Book;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

@Projection(name = "books", types = { Book.class })
public interface BookByIdProjection {


    Long getBookId();
    String getTitle();
    String getDescription();
    Integer getPages();
    String getIsbn();
    String getCover();
    String getFile();
    String getPublisher();
    String getAuthor();
    Long getCategory();
    String getCreatedBy();
    String getModifiedBy();
    Boolean getIsPrime();
    int getIsPrimeValue();
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss.ss",timezone = "IST")
    LocalDateTime getCreatedDate();
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime getModifiedDate();
    Boolean getIsDeleted();
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime getDeletedDate();
}
