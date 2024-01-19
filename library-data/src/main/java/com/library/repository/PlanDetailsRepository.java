package com.library.repository;

import com.library.entity.PlanDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanDetailsRepository extends JpaRepository<PlanDetails,Integer> {

    PlanDetails findByPlanId(String planId);

    Optional<PlanDetails> findById(Integer planId);
}
