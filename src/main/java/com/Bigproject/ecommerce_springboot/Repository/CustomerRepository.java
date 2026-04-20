package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.User;

@Repository
public interface CustomerRepository extends UserRepository {

	// Find all customers
	@Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER'")
	List<User> findAllCustomers();
	
	// Find customers by status (if applicable)
	@Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' AND u.status = ?1")
	List<User> findCustomersByStatus(String status);
	
	// Count total customers
	@Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CUSTOMER'")
	Long countCustomers();
	
	// Find customer by email with role check
	@Query("SELECT u FROM User u WHERE u.email = ?1 AND u.role = 'CUSTOMER'")
	User findCustomerByEmail(String email);
}
