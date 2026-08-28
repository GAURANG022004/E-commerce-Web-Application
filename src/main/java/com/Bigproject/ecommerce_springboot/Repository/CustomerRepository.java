package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.User;

@Repository
public interface CustomerRepository extends UserRepository {

    default List<User> findAllCustomers() {
        return findByRole("CUSTOMER");
    }

    default List<User> findCustomersByStatus(String status) {
        return findByRoleAndStatus("CUSTOMER", status);
    }

    default Long countCustomers() {
        return countByRole("CUSTOMER");
    }

    default User findCustomerByEmail(String email) {
        return findByEmailAndRole(email, "CUSTOMER");
    }
}