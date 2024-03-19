package com.library.pojo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BookResponse {

    private Long bookId;
    private String title;
    private String cover;
    private String file;
    private String description;
    private Integer pages;
    private String isbn;
    private String publisher;
    private String author;
    private Long category;
    private String createdBy;
    private String modifiedBy;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss.ss",timezone = "IST")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime modifiedDate;
    private Boolean IsDeleted;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime deletedDate;
    private int IsPrimeValue;
    private Boolean IsPrime;
}
