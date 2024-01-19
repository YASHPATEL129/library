package com.library.repository;


import com.library.entity.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyOrderRepository extends JpaRepository<MyOrder,Long> {

    MyOrder findByOrderId(String orderId);

}
