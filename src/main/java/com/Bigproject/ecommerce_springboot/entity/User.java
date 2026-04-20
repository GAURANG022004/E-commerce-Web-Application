
package com.Bigproject.ecommerce_springboot.entity;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_id;
	private String username;
	private String email;
	private String userpassword;
	private String role;
	private String status;
	
	
	
	
	public User() {
		super();
	}
	
	


	public Long getUser_id() {
		return user_id;
	}




	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}




	public String getUsername() {
		return username;
	}




	public void setUsername(String username) {
		this.username = username;
	}




	public String getUserpassword() {
		return userpassword;
	}




	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}




	public String getRole() {
		return role;
	}




	public void setRole(String role) {
		this.role = role;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public User( String email, String userpassword) {
		super();
		
		this.email = email;
		this.userpassword = userpassword;
	}




	public User(Long user_id, String username, String userpassword, String role) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.userpassword = userpassword;
		this.role = role;
	}




	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", username=" + username + ", email=" + email + ", userpassword="
				+ userpassword + ", role=" + role + ", status=" + status + "]";
	}


}
