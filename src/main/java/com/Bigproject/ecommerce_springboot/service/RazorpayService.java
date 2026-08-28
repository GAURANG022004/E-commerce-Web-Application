package com.Bigproject.ecommerce_springboot.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public String createRazorpayOrder(double amount, String receipt) throws Exception {

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        int amountInPaise = (int) Math.round(amount * 100);

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", receipt);

        com.razorpay.Order razorpayOrder =
                client.orders.create(orderRequest);

        return razorpayOrder.get("id");
    }

    public boolean verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature) {

        try {

            JSONObject options = new JSONObject();

            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            return Utils.verifyPaymentSignature(options, keySecret);

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public String getKeyId() {
        return keyId;
    }
}