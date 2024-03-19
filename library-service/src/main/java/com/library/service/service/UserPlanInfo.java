package com.library.service.service;

import com.library.entity.UserPlan;
import com.library.enums.IsStatus;
import com.library.pojo.response.IsStatusResponse;
import com.library.pojo.response.PlanDetailsResponse;
import com.library.pojo.response.TransactionInfo;
import com.library.interfaceProjections.TransactionInfoProjection;

import java.util.List;

public interface UserPlanInfo {

    List<IsStatusResponse> getUserPlan(IsStatus isStatus);
    
    List<PlanDetailsResponse> getOrdersWithPlanDetails(List<String> allowedStates);
}
