package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.Product;
import com.Bigproject.ecommerce_springboot.entity.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

// Search and filter
List<Product> findByCategoryIgnoreCase(String category);

List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String name,
        String description);

List<Product> findByNameContainingIgnoreCaseAndCategoryIgnoreCase(
        String name,
        String category);

// Dashboard / stock
List<Product> findByStockLessThanOrderByStockAsc(int stock);

List<Product> findTop5ByOrderByStockDesc();

// Retailer ownership
List<Product> findByRetailer(User retailer);

List<Product> findByRetailerUser_id(Long retailerId);

Product findByIdAndRetailerUser_id(Long id, Long retailerId);


}
