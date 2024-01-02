package com.library.repository;

import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByUserName(String userName);

    boolean existsByEmail(String email);

    User findByEmail(String email);


    UserRepository findUserByEmail(String email);
}
