package com.library.repository;


import com.library.entity.WebMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebMessageRepository extends JpaRepository<WebMessage,Long> {

}
