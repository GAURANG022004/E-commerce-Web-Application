package com.Bigproject.ecommerce_springboot.Controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bigproject.ecommerce_springboot.Repository.UserRepository;
import com.Bigproject.ecommerce_springboot.entity.User;
import com.Bigproject.ecommerce_springboot.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	

	
	@Autowired 
	UserService service;
	
	@Autowired
	UserRepository repo;
	
	@GetMapping("/")
	public String Start() {
		return "login";
	}
	
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}
	
	@PostMapping("/login")
	public String loginuser(@RequestParam String email, @RequestParam String userpassword, Model model, HttpSession session) {

		User users = service.chekUser(email, userpassword);

		if(users != null) {
			// Create Spring Security authentication token
			Authentication authentication = new UsernamePasswordAuthenticationToken(
				users.getEmail(),
				null,
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + users.getRole()))
			);

			// Set authentication in security context
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Store user in session
			session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
			session.setAttribute("user", users);
			model.addAttribute("user", users);

			// Redirect based on role
			String role = users.getRole();
			if("ADMIN".equals(role)) {
				return "redirect:/admin/dashboard";
			}
			else if("RETAILER".equals(role)) {
				// Check retailer approval status
				String status = users.getStatus();
				if("APPROVED".equals(status)) {
					return "redirect:/retailer/dashboard";
				}
				else if("PENDING".equals(status)) {
					model.addAttribute("message", "Your retailer account is pending approval. Please wait for admin approval.");
					return "retailer-pending";
				}
				else if("REJECTED".equals(status)) {
					model.addAttribute("error", "Your retailer application has been rejected. Please contact support.");
					return "retailer-rejected";
				}
				else {
					model.addAttribute("message", "Your account status is: " + status);
					return "retailer-pending";
				}
			}
			else if("CUSTOMER".equals(role)) {
				return "redirect:/customer/dashboard";
			}
			else {
				// Default redirect for unknown roles
				return "redirect:/products";
			}
		}
		else {
			model.addAttribute("error","Wrong Credentials");
			return "login";
		}

	}
	
	@GetMapping("/register")
    public String showRegistrationForm(Model model) {


		model.addAttribute("user", new User()); // <-- this line creates empty user object
        return "register"; // matches register.html
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user) {
        
    	service.registerUser(user);
    	
    	System.out.println("Email You entered in register :"+user);
        System.out.println("User Registered: " + user.getEmail() + " with role: " + user.getRole());
        return "redirect:/login"; // after successful registration
    }
    
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
	
	
	
}
