package com.Bigproject.ecommerce_springboot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByRazorpayPaymentId(String razorpayPaymentId);

    Payment findByRazorpayOrderId(String razorpayOrderId);
}