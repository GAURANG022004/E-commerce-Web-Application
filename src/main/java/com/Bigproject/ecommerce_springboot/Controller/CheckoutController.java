package com.Bigproject.ecommerce_springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bigproject.ecommerce_springboot.Repository.PaymentRepository;
import com.Bigproject.ecommerce_springboot.entity.Order;
import com.Bigproject.ecommerce_springboot.entity.Payment;
import com.Bigproject.ecommerce_springboot.entity.User;
import com.Bigproject.ecommerce_springboot.service.CartService;
import com.Bigproject.ecommerce_springboot.service.OrderService;
import com.Bigproject.ecommerce_springboot.service.RazorpayService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private PaymentRepository paymentRepository;

    // Create our order + Razorpay order
    @PostMapping("/checkout")
    public String checkout(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (cartService.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }

        try {

            Order order = orderService.createOrder(user);

            String razorpayOrderId =
                    razorpayService.createRazorpayOrder(
                            order.getTotalAmount(),
                            "ORDER_" + order.getId()
                    );

            order.setRazorpayOrderId(razorpayOrderId);

            orderService.saveOrder(order);

            return "redirect:/checkout/" + order.getId();

        } catch (Exception e) {

            e.printStackTrace();

            return "redirect:/cart";
        }
    }

    // Show Razorpay checkout page
    @GetMapping("/checkout/{orderId}")
    public String checkoutPage(
            @PathVariable Long orderId,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            return "redirect:/cart";
        }

        // Make sure customer can only access their own order
        if (!order.getCustomer().getUser_id().equals(user.getUser_id())) {
            return "redirect:/cart";
        }

        model.addAttribute("order", order);
        model.addAttribute("razorpayKeyId", razorpayService.getKeyId());

        return "checkout";
    }

    // Razorpay sends payment details here
    @PostMapping("/payment/verify")
    public String verifyPayment(
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpaySignature,
            @RequestParam Long orderId,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            return "redirect:/cart";
        }

        // Security check
        if (!order.getCustomer().getUser_id().equals(user.getUser_id())) {
            return "redirect:/cart";
        }

        // Use Razorpay order ID stored in OUR database
        if (!razorpayOrderId.equals(order.getRazorpayOrderId())) {
            return "redirect:/payment-failed";
        }

        boolean verified = razorpayService.verifyPayment(
                order.getRazorpayOrderId(),
                razorpayPaymentId,
                razorpaySignature
        );

        if (!verified) {
            return "redirect:/payment-failed";
        }

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setStatus("SUCCESS");

        paymentRepository.save(payment);

        orderService.markOrderCompleted(order);

        // Cart is cleared ONLY after successful payment
        cartService.clearCart();

        return "redirect:/order-success/" + order.getId();
    }

    @GetMapping("/order-success/{orderId}")
    public String orderSuccess(
            @PathVariable Long orderId,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(orderId);

        if (order == null ||
                !order.getCustomer().getUser_id().equals(user.getUser_id())) {

            return "redirect:/products";
        }

        model.addAttribute("order", order);

        return "order-success";
    }

    @GetMapping("/payment-failed")
    public String paymentFailed() {
        return "payment-failed";
    }
}