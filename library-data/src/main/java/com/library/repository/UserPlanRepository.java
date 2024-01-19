package com.library.repository;


import com.library.entity.User;
import com.library.entity.UserPlan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan,Long> {

//    String findByUserName(String userName);
    List<UserPlan> findByUserName(String userName);

    Boolean existsByUserName(String userName);




    @Query(value = "SELECT * FROM user_plan WHERE id = (SELECT MAX(id) FROM user_plan WHERE user_name = :userName)", nativeQuery = true)
    List<UserPlan> findMaxDateByUsername(@Param("userName") String userName);


}
