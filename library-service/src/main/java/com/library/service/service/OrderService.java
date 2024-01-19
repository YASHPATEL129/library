package com.library.service.service;


import com.razorpay.RazorpayException;

import java.util.Map;

public interface OrderService {

    Map<String, Object> createOrder(Map<String, Object> data) throws RazorpayException;

    Map<String, Object> updateOrder(Map<String, Object> data) throws RazorpayException;

}
