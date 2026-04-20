package com.Bigproject.ecommerce_springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Bigproject.ecommerce_springboot.Repository.UserRepository;
import com.Bigproject.ecommerce_springboot.Repository.RetailerRepository;
import com.Bigproject.ecommerce_springboot.Repository.AdminRepository;
import com.Bigproject.ecommerce_springboot.Repository.CustomerRepository;
import com.Bigproject.ecommerce_springboot.entity.User;

import java.util.Arrays;
import java.util.List;


@Service
public class UserService {
	
	@Autowired
	@Qualifier("userRepository")
	UserRepository repo;
	
	@Autowired
	RetailerRepository retailerRepository;
	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	private static final List<String> VALID_ROLES = Arrays.asList("CUSTOMER", "RETAILER", "ADMIN");

	public User chekUser(String email, String password) {
	    User user = repo.findByEmail(email);

	    if(user != null) {
	        if(passwordEncoder.matches(password, user.getUserpassword())) {
				// Check if retailer is approved
				if("RETAILER".equals(user.getRole())) {
					if(!"APPROVED".equals(user.getStatus())) {
						System.out.println("Retailer not approved: " + email + " - Status: " + user.getStatus());
						return null;
					}
				}
	            return user;
	        } else {
	            System.out.println("Password mismatch for email: " + email);
	        }
	    } else {
	        System.out.println("No user found with email: " + email);
	    }

	    return null;
	}
	
	public User registerUser(User user) {
	    String encodedPassword = passwordEncoder.encode(user.getUserpassword());
	    user.setUserpassword(encodedPassword);
	    
	    String role = user.getRole();
	    if(role == null || role.isEmpty() || !VALID_ROLES.contains(role.toUpperCase())) {
	        user.setRole("CUSTOMER");
	    } else {
	        user.setRole(role.toUpperCase());
	    }
	    
	    // Set status based on role
	    if("RETAILER".equals(user.getRole())) {
	    	user.setStatus("PENDING");
	    } else {
	    	user.setStatus("APPROVED");
	    }
	    
	    return repo.save(user);
	}
	
	public User registerUserWithRole(User user, String role) {
	    String encodedPassword = passwordEncoder.encode(user.getUserpassword());
	    user.setUserpassword(encodedPassword);
	    
	    if(role == null || role.isEmpty() || !VALID_ROLES.contains(role.toUpperCase())) {
	        user.setRole("CUSTOMER");
	    } else {
	        user.setRole(role.toUpperCase());
	    }
	    
	    // Set status based on role
	    if("RETAILER".equals(user.getRole())) {
	    	user.setStatus("PENDING");
	    } else {
	    	user.setStatus("APPROVED");
	    }
	    
	    return repo.save(user);
	}
	
	// Admin approves retailer
	public User approveRetailer(Long userId) {
		User user = retailerRepository.findById(userId).orElse(null);
		if(user != null) {
			user.setStatus("APPROVED");
			return retailerRepository.save(user);
		}
		return null;
	}
	
	// Admin rejects retailer
	public User rejectRetailer(Long userId) {
		User user = retailerRepository.findById(userId).orElse(null);
		if(user != null) {
			user.setStatus("REJECTED");
			return retailerRepository.save(user);
		}
		return null;
	}
	
	// Get all pending retailers
	public List<User> getPendingRetailers() {
		return retailerRepository.findPendingRetailers();
	}
	
	// Get all approved retailers
	public List<User> getApprovedRetailers() {
		return retailerRepository.findApprovedRetailers();
	}
	
	// Get all rejected retailers
	public List<User> getRejectedRetailers() {
		return retailerRepository.findRejectedRetailers();
	}
	
	// Get all retailers
	public List<User> getAllRetailers() {
		return retailerRepository.findAllRetailers();
	}
	
	// Admin creates approved retailer directly
	public User createApprovedRetailer(User user) {
		String encodedPassword = passwordEncoder.encode(user.getUserpassword());
	    user.setUserpassword(encodedPassword);
	    user.setRole("RETAILER");
	    user.setStatus("APPROVED");
	    return retailerRepository.save(user);
	}
	
	// Get admin dashboard stats
	public Long countTotalCustomers() {
		return adminRepository.countTotalCustomers();
	}
	
	public Long countTotalRetailers() {
		return adminRepository.countTotalRetailers();
	}
	
	public Long countPendingRetailers() {
		return adminRepository.countPendingRetailers();
	}
	
	public Long countApprovedRetailers() {
		return adminRepository.countApprovedRetailers();
	}
	
	// Customer management methods using CustomerRepository
	public List<User> getAllCustomers() {
		return customerRepository.findAllCustomers();
	}
	
	public List<User> getCustomersByStatus(String status) {
		return customerRepository.findCustomersByStatus(status);
	}
	
	public Long countCustomers() {
		return customerRepository.countCustomers();
	}
	
	public User getCustomerByEmail(String email) {
		return customerRepository.findCustomerByEmail(email);
	}


}
