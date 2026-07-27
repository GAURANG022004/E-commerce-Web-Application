package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	@Query("SELECT p FROM Product p WHERE " +
		       "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR "+
			"(LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) ")
		List<Product> searchByOptionalParams(String keyword);

	List<Product> findByCategoryIgnoreCase(String category);

	// Count total products
	@Query("SELECT COUNT(p) FROM Product p")
	Long countTotalProducts();

	// Get products with low stock
	@Query("SELECT p FROM Product p WHERE p.stock < 20 ORDER BY p.stock ASC")
	List<Product> findLowStockProducts();

	// Get popular products (highest stock for demo - in real app would be by sales)
	@Query("SELECT p FROM Product p ORDER BY p.stock DESC LIMIT 5")
	List<Product> findPopularProducts();

    @Query("SELECT p FROM Product p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
           "(LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND LOWER(p.category) = LOWER(:category)")
    List<Product> searchByKeywordAndCategory(String keyword, String category);			


}
