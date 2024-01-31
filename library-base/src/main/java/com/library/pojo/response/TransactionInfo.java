package com.library.pojo.response;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInfo {

    private Long myOrderId;
    private String orderId;
    private String amount;
    private String userName;
    private String status;
    private String paymentId;
    private Instant paymentDate;
    private Integer plan_id;
    private Instant createdTime;
    private String planName;
    private int months;
    private String planId;



}
