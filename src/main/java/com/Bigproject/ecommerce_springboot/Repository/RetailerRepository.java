package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.User;

@Repository
public interface RetailerRepository extends UserRepository {

	// Find all retailers
	@Query("SELECT u FROM User u WHERE u.role = 'RETAILER'")
	List<User> findAllRetailers();
	
	// Find pending retailers
	@Query("SELECT u FROM User u WHERE u.role = 'RETAILER' AND u.status = 'PENDING'")
	List<User> findPendingRetailers();
	
	// Find approved retailers
	@Query("SELECT u FROM User u WHERE u.role = 'RETAILER' AND u.status = 'APPROVED'")
	List<User> findApprovedRetailers();
	
	// Find rejected retailers
	@Query("SELECT u FROM User u WHERE u.role = 'RETAILER' AND u.status = 'REJECTED'")
	List<User> findRejectedRetailers();
	
	// Count retailers by status
	@Query("SELECT COUNT(u) FROM User u WHERE u.role = 'RETAILER' AND u.status = ?1")
	Long countRetailersByStatus(String status);
	
	// Count total retailers
	@Query("SELECT COUNT(u) FROM User u WHERE u.role = 'RETAILER'")
	Long countRetailers();
	
	// Find retailer by email with role check
	@Query("SELECT u FROM User u WHERE u.email = ?1 AND u.role = 'RETAILER'")
	User findRetailerByEmail(String email);
	
	// Find retailer by email and status
	@Query("SELECT u FROM User u WHERE u.email = ?1 AND u.role = 'RETAILER' AND u.status = ?2")
	User findRetailerByEmailAndStatus(String email, String status);
}
