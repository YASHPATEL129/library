package com.library.repository;


import com.library.entity.Book;
import com.library.interfaceProjections.BookByIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(nativeQuery = true, value = "SELECT b.book_id AS bookId," +
            "b.title AS title, " +
            "b.description AS description," +
            "b.pages AS pages, " +
            "b.isbn AS isbn, " +
            "b.publisher AS publisher," +
            "b.author AS author," +
            " b.category AS category," +
            " b.created_by AS createdBy," +
            " b.modified_by AS modifiedBy, " +
            "b.created_date AS createdDate," +
            " b.modified_date AS modifiedDate," +
            " b.is_deleted AS isDeleted," +
            " b.deleted_date AS deletedDate, " +
            "b.is_prime AS isPrime, "
            + "i.new_image_name AS cover, a.new_filename AS file, "
            + "IF(b.is_prime = 0, "
            + "   true, (SELECT EXISTS(SELECT 1 FROM user_plan up WHERE up.is_status = 'CURRENT' AND up.user_name = :username))) "
            + "    AS isPrimeValue "
            + "FROM Book b "
            + "LEFT JOIN admin_image i ON b.book_id = i.bind_id "
            + "LEFT JOIN Attachment a ON b.book_id = a.bind_id "
            + "WHERE b.book_id = :bookId")
    BookByIdProjection getBookById(Long bookId , String username);




}
