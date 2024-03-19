package com.library.repository;

import com.library.entity.PlanDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanDetailsRepository extends JpaRepository<PlanDetails,Integer> {

    PlanDetails findByPlanId(String planId);

    Optional<PlanDetails> findById(Integer planId);

    @Query("SELECT p FROM PlanDetails p ORDER BY p.position")
    List<PlanDetails> findAllOrderedByPosition();
}
