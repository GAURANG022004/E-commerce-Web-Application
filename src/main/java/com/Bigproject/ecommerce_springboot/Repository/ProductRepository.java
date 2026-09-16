package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

        @Query("SELECT p FROM Product p WHERE p.retailer.user_id = :retailerId")
        List<Product> findProductsByRetailerId(@Param("retailerId") Long retailerId);

        @Query("SELECT p FROM Product p WHERE p.id = :id AND p.retailer.user_id = :retailerId")
        Product findProductByIdAndRetailerId(
                        @Param("id") Long id,
                        @Param("retailerId") Long retailerId);

}
