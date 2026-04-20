package com.Bigproject.ecommerce_springboot.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Count total orders
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'COMPLETED'")
    Long countTotalOrders();

    // Count today's orders
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'COMPLETED' AND DATE(o.orderDate) = CURRENT_DATE")
    Long countTodayOrders();

    // Sum total sales
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'COMPLETED'")
    Double sumTotalSales();

    // Sum today's sales
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status = 'COMPLETED' AND DATE(o.orderDate) = CURRENT_DATE")
    Double sumTodaySales();

    // Average sales (average order value)
    @Query("SELECT COALESCE(AVG(o.totalAmount), 0) FROM Order o WHERE o.status = 'COMPLETED'")
    Double getAverageOrderValue();

    // Get monthly sales for chart (last 12 months)
    @Query("SELECT EXTRACT(MONTH FROM o.orderDate) as month, COALESCE(SUM(o.totalAmount), 0) as total " +
           "FROM Order o WHERE o.status = 'COMPLETED' AND o.orderDate >= :startDate " +
           "GROUP BY EXTRACT(MONTH FROM o.orderDate) ORDER BY month")
    List<Object[]> getMonthlySales(@Param("startDate") LocalDateTime startDate);

    // Get recent orders (last 5)
    @Query("SELECT o FROM Order o WHERE o.status = 'COMPLETED' ORDER BY o.orderDate DESC LIMIT 5")
    List<Order> findRecentOrders();

    // Count orders by date range for chart
    @Query("SELECT EXTRACT(MONTH FROM o.orderDate) as month, COUNT(o) as count " +
           "FROM Order o WHERE o.status = 'COMPLETED' AND o.orderDate >= :startDate " +
           "GROUP BY EXTRACT(MONTH FROM o.orderDate) ORDER BY month")
    List<Object[]> getMonthlyOrderCounts(@Param("startDate") LocalDateTime startDate);

    // Get popular products by order count
    @Query("SELECT o FROM Order o WHERE o.status = 'COMPLETED' ORDER BY o.orderDate DESC")
    List<Order> findAllCompletedOrders();
}
