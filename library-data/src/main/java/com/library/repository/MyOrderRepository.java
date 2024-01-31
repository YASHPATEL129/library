package com.library.repository;


import com.library.entity.MyOrder;
import com.library.interfaceProjections.TransactionInfoProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MyOrderRepository extends JpaRepository<MyOrder,Long> {

    MyOrder findByOrderId(String orderId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM orders o WHERE o.status = 'created' AND o.created_time < :oneHourAgo")
    void deleteOldCreateOrders(Instant oneHourAgo);

    List<MyOrder> findByUserNameAndStatusIn(String userName, List<String> statusList);

//
//    @Query(nativeQuery = true, value =
//            "SELECT o.my_order_id AS myOrderId, o.amount AS amount,o.order_id AS orderId,o.payment_id AS paymentId, pd.plan_name AS planName , pd.plan_Id AS planId, pd.months AS months " +
//            "FROM orders o " +
//            "JOIN plan_details pd ON o.plan_id = pd.id " +
//            "WHERE o.user_name = :userName AND o.status IN :statusList")
//    List<TransactionInfo> findOrdersWithPlanDetails(
//            @Param("userName") String userName,
//            @Param("statusList") List<String> statusList);
//    o.status ,o.user_name,o.payment_date,o.plan_id,o.created_time,

    @Query(nativeQuery = true, value =
            "SELECT o.my_order_id, o.order_id, o.amount, o.user_name, o.status, o.payment_id, " +
            "o.payment_date, o.plan_id, o.created_time, pd.plan_name, pd.months , pd.plan_Id AS planIdFromPlanDetails " +
            "FROM orders o " +
            "JOIN plan_details pd ON o.plan_id = pd.id " +
            "WHERE o.user_name = :userName AND o.status IN :statusList")
    List<TransactionInfoProjection> findOrdersWithPlanDetails(
            @Param("userName") String userName,
            @Param("statusList") List<String> statusList);
}
