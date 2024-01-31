package com.library.service.service.impl;

import com.library.entity.MyOrder;
import com.library.entity.PlanDetails;
import com.library.entity.UserPlan;
import com.library.enums.IsActiveType;
import com.library.pojo.CurrentSession;
import com.library.pojo.response.TransactionInfo;
import com.library.interfaceProjections.TransactionInfoProjection;
import com.library.repository.MyOrderRepository;
import com.library.repository.PlanDetailsRepository;
import com.library.repository.UserPlanRepository;
import com.library.service.service.UserPlanInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public List<UserPlan> getUserPlan(IsActiveType is_active) {
        String userName = currentSession.getUserName();
        return userPlanRepository.findByUserNameAndIsActive(userName, is_active);
    }

//    @Override
//    public List<TransactionInfo> getUserTransactions(List<String> statusList) {
//        String userName = currentSession.getUserName();
//        List<String> allowedStates = Arrays.asList("success", "failed");
//        List<MyOrder> myOrders = myOrderRepository.findByUserNameAndStatusIn(userName,allowedStates);
//        List<TransactionInfo> transactionInfos = new ArrayList<>();
//
//        for (MyOrder myOrder : myOrders) {
//            TransactionInfo transactionInfo = new TransactionInfo();
//            transactionInfo.setMyOrderId(myOrder.getMyOrderId());
//            transactionInfo.setOrderId(myOrder.getOrderId());
//            transactionInfo.setAmount(myOrder.getAmount());
//            transactionInfo.setUserName(userName);
//            transactionInfo.setStatus(myOrder.getStatus());
//            transactionInfo.setPaymentId(myOrder.getPaymentId());
//            transactionInfo.setPaymentDate(myOrder.getPaymentDate());
//            transactionInfo.setPlan_id(myOrder.getPlan_id());
//            transactionInfo.setCreatedTime(myOrder.getCreatedTime());
//
//            if (myOrder.getPlan_id() != null) {
//                Optional<PlanDetails> planDetailsOptional = planDetailsRepository.findById(myOrder.getPlan_id());
//                if (planDetailsOptional.isPresent()) {
//                    PlanDetails planDetails = planDetailsOptional.get();
//                    transactionInfo.setPlanName(planDetails.getPlanName());
//                    transactionInfo.setMonths(planDetails.getMonths());
//                    transactionInfo.setPlanId(planDetails.getPlanId());
//                }
//            }
//            transactionInfos.add(transactionInfo);
//        }
//        System.out.println(transactionInfos);
//        return transactionInfos;
//    }

    @Override
    public List<TransactionInfoProjection> getOrdersWithPlanDetails(List<String> statusList) {
        String userName = currentSession.getUserName();
        List<String> allowedStates = Arrays.asList("success", "failed");
        return myOrderRepository.findOrdersWithPlanDetails(userName,allowedStates);
    }
}
