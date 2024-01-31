package com.library.interfaceProjections;

import com.library.entity.MyOrder;
import org.springframework.data.rest.core.config.Projection;

import java.time.Instant;

@Projection(name = "transactionInfo", types = { MyOrder.class })
public interface TransactionInfoProjection {

    Long getMy_order_id();
    String getOrder_id();
    String getAmount();
    String getUser_name();
    String getStatus();
    String getPayment_id();
    Instant getPayment_date();
    Integer getPlan_id();
    Instant getCreated_time();
    String getPlan_name();
    int getMonths();
    String getPlanIdFromPlanDetails();
}
