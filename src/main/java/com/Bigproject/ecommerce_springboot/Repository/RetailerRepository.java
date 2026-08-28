package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.User;

@Repository
public interface RetailerRepository extends UserRepository {

    User findByEmailAndRole(String email, String role);

    User findByEmailAndRoleAndStatus(
            String email,
            String role,
            String status
    );

    default List<User> findPendingRetailers() {
        return findByRoleAndStatus("RETAILER", "PENDING");
    }

    default List<User> findApprovedRetailers() {
        return findByRoleAndStatus("RETAILER", "APPROVED");
    }

    default List<User> findRejectedRetailers() {
        return findByRoleAndStatus("RETAILER", "REJECTED");
    }

    default List<User> findAllRetailers() {
        return findByRole("RETAILER");
    }
}