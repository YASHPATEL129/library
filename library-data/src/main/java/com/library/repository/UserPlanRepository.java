package com.library.repository;


import com.library.entity.User;
import com.library.entity.UserPlan;
import com.library.enums.IsActiveType;
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

    @Query(value = "SELECT * FROM user_plan WHERE id = (SELECT MAX(id) FROM user_plan WHERE user_name = :userName)", nativeQuery = true)
    List<UserPlan> findMaxDateByUsername(@Param("userName") String userName);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value =
            "UPDATE user_plan up SET up.is_active = " +
            "CASE " +
            "    WHEN CURRENT_DATE > up.ending_date THEN 'EXPIRY' " +
            "    WHEN CURRENT_DATE = up.starting_date AND up.is_active = 'UPCOMING' THEN 'CURRENT' " +
            "    ELSE UP.is_active " +
            "    END " +
            "WHERE is_active IN ('UPCOMING', 'CURRENT')")
    void updateIsActiveBasedOnDate();

    List<UserPlan> findByUserNameAndIsActive(String userName, IsActiveType is_active);
}
