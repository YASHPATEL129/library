package com.library.service.service;

import com.library.entity.UserPlan;
import com.library.enums.IsActiveType;
import com.library.pojo.response.TransactionInfo;
import com.library.interfaceProjections.TransactionInfoProjection;

import java.util.List;

public interface UserPlanInfo {

    List<UserPlan> getUserPlan(IsActiveType is_active);
    
    List<TransactionInfoProjection> getOrdersWithPlanDetails(List<String> allowedStates);
}
