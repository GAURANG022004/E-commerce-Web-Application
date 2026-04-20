package com.Bigproject.ecommerce_springboot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Bigproject.ecommerce_springboot.Repository.OrderRepository;
import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.Repository.UserRepository;
import com.Bigproject.ecommerce_springboot.entity.Order;
import com.Bigproject.ecommerce_springboot.entity.Product;

@Service
public class DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Get total orders count
    public Long getTotalOrders() {
        Long count = orderRepository.countTotalOrders();
        return count != null ? count : 0L;
    }

    // Get total sales
    public Double getTotalSales() {
        Double sales = orderRepository.sumTotalSales();
        return sales != null ? sales : 0.0;
    }

    // Get today's orders
    public Long getTodayOrders() {
        Long count = orderRepository.countTodayOrders();
        return count != null ? count : 0L;
    }

    // Get today's sales
    public Double getTodaySales() {
        Double sales = orderRepository.sumTodaySales();
        return sales != null ? sales : 0.0;
    }

    // Get average order value
    public Double getAverageOrderValue() {
        Double avg = orderRepository.getAverageOrderValue();
        return avg != null ? avg : 0.0;
    }

    // Get total products count
    public Long getTotalProducts() {
        Long count = productRepository.countTotalProducts();
        return count != null ? count : 0L;
    }

    // Get total customers count
    public Long getTotalCustomers() {
        Long count = userRepository.countByRole("CUSTOMER");
        return count != null ? count : 0L;
    }

    // Get recent orders
    public List<Order> getRecentOrders() {
        List<Order> orders = orderRepository.findRecentOrders();
        return orders != null ? orders : new ArrayList<>();
    }

    // Get popular products
    public List<Product> getPopularProducts() {
        List<Product> products = productRepository.findPopularProducts();
        return products != null ? products : new ArrayList<>();
    }

    // Get monthly sales data for chart (last 12 months)
    public Map<String, Double> getMonthlySalesData() {
        Map<String, Double> monthlyData = new HashMap<>();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        // Initialize all months with 0
        for (int i = 0; i < 12; i++) {
            monthlyData.put(months[i], 0.0);
        }

        // Get data from last 12 months
        LocalDateTime startDate = LocalDateTime.now().minusMonths(12);
        List<Object[]> results = orderRepository.getMonthlySales(startDate);

        if (results != null) {
            for (Object[] result : results) {
                Integer month = ((Number) result[0]).intValue();
                Double amount = ((Number) result[1]).doubleValue();
                if (month >= 1 && month <= 12) {
                    monthlyData.put(months[month - 1], amount);
                }
            }
        }

        return monthlyData;
    }

    // Get low stock products
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }
}
