package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.User;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long> {

	//finding user by username, email, role, and status
    User findByUsername(String username);

    User findByEmail(String email);

    User findByEmailAndUserpassword(String email, String password);

	User findByEmailAndRole(String email, String role);

    List<User> findByRole(String role);
	
	List<User> findByRoleNot(String role);

    List<User> findByRoleAndStatus(String role, String status);

    long countByRole(String role);

    long countByRoleAndStatus(String role, String status);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}