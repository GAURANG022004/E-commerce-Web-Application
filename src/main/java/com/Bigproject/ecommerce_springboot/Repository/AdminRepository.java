package com.Bigproject.ecommerce_springboot.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.Bigproject.ecommerce_springboot.entity.User;

@Repository
public interface AdminRepository extends UserRepository {

    default List<User> findAllAdmins() {
        return findByRole("ADMIN");
    }

    default Long countAdmins() {
        return countByRole("ADMIN");
    }

    default User findAdminByEmail(String email) {
        return findByEmailAndRole(email, "ADMIN");
    }

    default Long countTotalCustomers() {
        return countByRole("CUSTOMER");
    }

    default Long countTotalRetailers() {
        return countByRole("RETAILER");
    }

    default Long countPendingRetailers() {
        return countByRoleAndStatus("RETAILER", "PENDING");
    }

    default Long countApprovedRetailers() {
        return countByRoleAndStatus("RETAILER", "APPROVED");
    }

    default List<User> findAllNonAdminUsers() {
        return findByRoleNot("ADMIN");
    }
}