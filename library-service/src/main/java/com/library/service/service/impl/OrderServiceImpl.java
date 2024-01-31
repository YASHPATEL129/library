package com.library.service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.entity.MyOrder;
import com.library.entity.PlanDetails;
import com.library.entity.UserPlan;
import com.library.enums.IsActiveType;
import com.library.pojo.CurrentSession;
import com.library.repository.MyOrderRepository;
import com.library.repository.PlanDetailsRepository;
import com.library.repository.UserPlanRepository;
import com.library.service.service.OrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${key}")
    String KEY;

    @Value("${key.secret}")
    String KEY_SECRET;

    @Value("${current}")
    String CURRENT;

    @Autowired
    private MyOrderRepository myOrderRepository;

    @Autowired
    private UserPlanRepository userPlanRepository;

    @Autowired
    private CurrentSession currentSession;

    @Autowired
    private PlanDetailsRepository planDetailsRepository;

    @Override
    public Map<String, Object> createOrder(Map<String, Object> data) throws RazorpayException{

        String planId = data.get("plan_id").toString();
        if (planId == null){
            throw new RuntimeException();
        }
        PlanDetails planDetails = planDetailsRepository.findByPlanId(planId);

        RazorpayClient client = new RazorpayClient(KEY, KEY_SECRET);
        JSONObject object = new JSONObject();
        object.put("amount", planDetails.getAmount());
        object.put("currency", CURRENT);
        //create new order
        Order order = client.orders.create(object);
        System.out.println(order);
        //save order in database
        MyOrder myOrder = new MyOrder();
        myOrder.setAmount(String.valueOf(planDetails.getAmount()));
        myOrder.setOrderId(order.get("id"));
        myOrder.setPaymentId(null);
        myOrder.setStatus("created");
        myOrder.setUserName(currentSession.getUserName());
        myOrder.setPaymentDate(Instant.now().truncatedTo(ChronoUnit.DAYS));
        myOrder.setPlan_id(planDetails.getId());
        this.myOrderRepository.save(myOrder);
        try {
            return new ObjectMapper().readValue(order.toString(), new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    @Override
    public Map<String, Object> updateOrder(Map<String, Object> data) throws RazorpayException {
        MyOrder myOrder = this.myOrderRepository.findByOrderId(data.get("order_id").toString());
        myOrder.setPaymentId(data.get("payment_id").toString());
        boolean isSuccess = false;
        Optional<PlanDetails> planDetails = planDetailsRepository.findById(myOrder.getPlan_id());
        if (ObjectUtils.isNotEmpty(data.get("razorpay_signature"))) {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", data.get("order_id").toString());
            options.put("razorpay_payment_id", data.get("payment_id").toString());
            options.put("razorpay_signature", data.get("razorpay_signature").toString());
            isSuccess = Utils.verifyPaymentSignature(options, KEY_SECRET);
            myOrder.setStatus(isSuccess ? "success" : "failed");
            myOrder.setPaymentDate(Instant.now().truncatedTo(ChronoUnit.DAYS));
        } else {
            myOrder.setStatus("failed");
        }
        this.myOrderRepository.save(myOrder);
            if (myOrder.getStatus() == "success") {
                UserPlan userPlan = new UserPlan();
                List<UserPlan> lastPlan = userPlanRepository.findMaxDateByUsername(myOrder.getUserName());
                System.out.println(lastPlan);
                userPlan.setUserName(myOrder.getUserName());
                userPlan.setMy_order_id(myOrder.getMyOrderId());
                userPlan.setPlan_id(myOrder.getPlan_id());
                setData(userPlan, lastPlan, planDetails);
                userPlanRepository.save(userPlan);
            }
        return Map.of("status", isSuccess);
    }

    @Override
    public List<PlanDetails> getAllPlanDetails(HttpServletRequest request) {
        try{
            return planDetailsRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    private void setData(UserPlan userPlan, List<UserPlan> lastPlan , Optional<PlanDetails> planDetails){
        int month = planDetails.get().getMonths();
        if (lastPlan.isEmpty()){
            userPlan.setIsActive(IsActiveType.CURRENT);
            userPlan.setStartingDate(Instant.now().truncatedTo(ChronoUnit.DAYS));
            userPlan.setEndingDate(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(month * 30L, ChronoUnit.DAYS));
            return;
        }
        switch (lastPlan.get(0).getIsActive()) {
            case UPCOMING,CURRENT -> {
                userPlan.setIsActive(IsActiveType.UPCOMING);
                userPlan.setStartingDate(lastPlan.get(0).getEndingDate());
                userPlan.setEndingDate(userPlan.getStartingDate().plus(month * 30L, ChronoUnit.DAYS));
            }
            case EXPIRY -> {
                userPlan.setIsActive(IsActiveType.CURRENT);
                userPlan.setStartingDate(Instant.now().truncatedTo(ChronoUnit.DAYS));
                userPlan.setEndingDate(Instant.now().plus(month * 30L, ChronoUnit.DAYS));
            }

        }
    }

}