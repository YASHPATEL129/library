package com.library.pojo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlanInfo {

    private Integer id;
    private String planName;
    private Long amount;
    private int months;
    private String planId;
    private double pricePerMonth;
    private String planTypes;
}
