package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.User;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long>{

	User findByUsername(String username);

	User findByEmail(String email);
	User findByemail(String email);

	User findByEmailAndUserpassword(String email, String password);
	
	// Role-based queries
	List<User> findByRole(String role);
	
	List<User> findByRoleAndStatus(String role, String status);
	
	// Admin specific queries
	@Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
	List<User> findAllAdmins();
	
	// Customer specific queries
	@Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER'")
	List<User> findAllCustomers();
	
	// Retailer specific queries
	@Query("SELECT u FROM User u WHERE u.role = 'RETAILER'")
	List<User> findAllRetailers();
	
	@Query("SELECT u FROM User u WHERE u.role = 'RETAILER' AND u.status = 'PENDING'")
	List<User> findPendingRetailers();
	
	@Query("SELECT u FROM User u WHERE u.role = 'RETAILER' AND u.status = 'APPROVED'")
	List<User> findApprovedRetailers();
	
	@Query("SELECT u FROM User u WHERE u.role = 'RETAILER' AND u.status = 'REJECTED'")
	List<User> findRejectedRetailers();
	
	// Count queries for admin dashboard
	Long countByRole(String role);
	
	Long countByRoleAndStatus(String role, String status);
	
	// Check if email exists
	boolean existsByEmail(String email);
	
	// Check if username exists
	boolean existsByUsername(String username);

}
