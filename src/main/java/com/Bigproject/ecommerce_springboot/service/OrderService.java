package com.Bigproject.ecommerce_springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Bigproject.ecommerce_springboot.Repository.OrderRepository;
import com.Bigproject.ecommerce_springboot.entity.Cart;
import com.Bigproject.ecommerce_springboot.entity.Order;
import com.Bigproject.ecommerce_springboot.entity.User;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    public Order createOrder(User customer) {

        List<Cart> cartItems = cartService.getCartItems();

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();

        order.setCustomer(customer);

        order.setTotalAmount(cartService.getTotal() + 50);

        int totalItems = 0;

        for (Cart item : cartItems) {
            totalItems += item.getQuantity();
        }

        order.setTotalItems(totalItems);

        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void markOrderCompleted(Order order) {
        order.setStatus("COMPLETED");
        orderRepository.save(order);
    }

    public List<Order> getCustomerOrders(User customer) {
        return orderRepository.findByCustomerOrderByOrderDateDesc(customer);
    }
}