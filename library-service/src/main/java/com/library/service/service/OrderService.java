package com.library.service.service;


import com.library.entity.PlanDetails;
import com.library.pojo.response.PlanInfo;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Map<String, Object> createOrder(Map<String, Object> data) throws RazorpayException;

    Map<String, Object> updateOrder(Map<String, Object> data) throws RazorpayException;

    List<PlanDetails> getAllPlanDetails(HttpServletRequest request);

}
