package com.library.service.service.impl;

import com.library.entity.UserPlan;
import com.library.enums.IsStatus;
import com.library.interfaceProjections.TransactionInfoProjection;
import com.library.interfaceProjections.UserProfileProjection;
import com.library.pojo.CurrentSession;
import com.library.pojo.response.IsStatusResponse;
import com.library.pojo.response.PlanDetailsResponse;
import com.library.repository.MyOrderRepository;
import com.library.repository.PlanDetailsRepository;
import com.library.repository.UserPlanRepository;
import com.library.service.service.UserPlanInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPlanInfoImpl implements UserPlanInfo {

    @Autowired
    private UserPlanRepository userPlanRepository;

    @Autowired
    private MyOrderRepository myOrderRepository;

    @Autowired
    private CurrentSession currentSession;

    @Autowired
    private PlanDetailsRepository planDetailsRepository;

    @Override
    public List<IsStatusResponse> getUserPlan(IsStatus isStatus) {
        String userName = currentSession.getUserName();
        return userPlanRepository.getUserProfileDetails(userName, isStatus.toString())
                .stream().sorted(Comparator.comparing(UserProfileProjection::getStartingDate, Comparator.naturalOrder())).map(e -> new IsStatusResponse()
                        .setStartingDate(e.getStartingDate())
                        .setEndingDate(e.getEndingDate())
                        .setPlanName(e.getPlanName())
                        .setOrderId(e.getOrderId())).collect(Collectors.toList());
    }

    @Override
    public List<PlanDetailsResponse> getOrdersWithPlanDetails(List<String> statusList) {
        String userName = currentSession.getUserName();
        List<String> allowedStates = Arrays.asList("success", "failed");
        return myOrderRepository.findOrdersWithPlanDetails(userName,allowedStates)
                .stream().sorted(Comparator.comparing(TransactionInfoProjection::getPayment_date, Comparator.naturalOrder())).map(e -> new PlanDetailsResponse()
                        .setOrderId(e.getOrder_id())
                        .setPlanName(e.getPlan_name())
                        .setStatus(e.getStatus())
                        .setAmount(e.getAmount())
                        .setPaymentDate(e.getPayment_date())).collect(Collectors.toList());
    }
}
