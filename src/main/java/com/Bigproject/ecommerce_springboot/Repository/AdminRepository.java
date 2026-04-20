package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.User;

@Repository
public interface AdminRepository extends UserRepository {

	// Find all admins
	@Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
	List<User> findAllAdmins();
	
	// Count total admins
	@Query("SELECT COUNT(u) FROM User u WHERE u.role = 'ADMIN'")
	Long countAdmins();
	
	// Find admin by email with role check
	@Query("SELECT u FROM User u WHERE u.email = ?1 AND u.role = 'ADMIN'")
	User findAdminByEmail(String email);
	
	// Dashboard statistics
	@Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CUSTOMER'")
	Long countTotalCustomers();
	
	@Query("SELECT COUNT(u) FROM User u WHERE u.role = 'RETAILER'")
	Long countTotalRetailers();
	
	@Query("SELECT COUNT(u) FROM User u WHERE u.role = 'RETAILER' AND u.status = 'PENDING'")
	Long countPendingRetailers();
	
	@Query("SELECT COUNT(u) FROM User u WHERE u.role = 'RETAILER' AND u.status = 'APPROVED'")
	Long countApprovedRetailers();
	
	// Get all users except admins (for admin management)
	@Query("SELECT u FROM User u WHERE u.role != 'ADMIN'")
	List<User> findAllNonAdminUsers();
}
