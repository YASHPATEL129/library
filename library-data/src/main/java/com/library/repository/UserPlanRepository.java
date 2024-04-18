package com.library.repository;


import com.library.entity.UserPlan;
import com.library.interfaceProjections.UserProfileProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan,Long> {

    @Query(value = "SELECT * FROM user_plan WHERE id = (SELECT MAX(id) FROM user_plan WHERE user_name = :userName)", nativeQuery = true)
    List<UserPlan> findMaxDateByUsername(@Param("userName") String userName);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value =
            "UPDATE user_plan up SET up.is_status = " +
            "CASE " +
            "    WHEN CURRENT_DATE > up.ending_date THEN 'EXPIRY' " +
            "    WHEN CURRENT_DATE = up.starting_date AND up.is_status = 'UPCOMING' THEN 'CURRENT' " +
            "    ELSE UP.is_status " +
            "    END " +
            "WHERE is_status IN ('UPCOMING', 'CURRENT')")
    void updateIsActiveBasedOnDate();




    @Query( nativeQuery = true,value =
            "SELECT u.starting_date as startingDate, u.ending_date as endingDate," +
                    "o.order_id as orderId, pd.plan_name as planName "+
                    "FROM user_plan u " +
                    "LEFT JOIN orders o ON o.my_order_id = u.my_order_id " +
                    "LEFT JOIN plan_details pd ON pd.id = u.plan_id " +
                    "WHERE u.user_name = :userName AND u.is_status = :status ORDER BY u.starting_date")
    List<UserProfileProjection> getUserProfileDetails(String userName, String status);
}
