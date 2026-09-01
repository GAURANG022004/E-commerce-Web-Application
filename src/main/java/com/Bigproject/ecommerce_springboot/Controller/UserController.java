package com.Bigproject.ecommerce_springboot.Controller;

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

import java.util.Collections;

@Controller
public class UserController {


@Autowired
private UserService service;

@Autowired
private UserRepository repo;

@GetMapping("/")
public String start() {
    return "login";
}

@GetMapping("/login")
public String showLoginPage() {
    return "login";
}

// @PostMapping("/login")
// public String loginUser(
//         @RequestParam String email,
//         @RequestParam String userpassword,
//         Model model,
//         HttpSession session) {

//     User user = service.chekUser(email, userpassword);

//     if (user == null) {
//         model.addAttribute("error", "Wrong email or password.");
//         return "login";
//     }

//     // Create authenticated user for Spring Security
//     Authentication authentication =
//             new UsernamePasswordAuthenticationToken(
//                     user.getEmail(),
//                     null,
//                     Collections.singletonList(
//                             new SimpleGrantedAuthority(
//                                     "ROLE_" + user.getRole()
//                             )
//                     )
//             );

//     SecurityContextHolder.getContext()
//             .setAuthentication(authentication);

//     // Store application user in session
//     session.setAttribute("user", user);

//     // Redirect according to role
//     if ("ADMIN".equals(user.getRole())) {
//         return "redirect:/admin/dashboard";
//     }

//     if ("RETAILER".equals(user.getRole())) {
//         return "redirect:/retailer/dashboard";
//     }

//     if ("CUSTOMER".equals(user.getRole())) {
//         return "redirect:/customer/dashboard";
//     }

//     // Unknown role
//     SecurityContextHolder.clearContext();
//     session.invalidate();

//     model.addAttribute("error", "Invalid user role.");
//     return "login";
// }



@GetMapping("/register")
public String showRegistrationForm(Model model) {

    model.addAttribute("user", new User());

    return "register";

}

@PostMapping("/register")
public String processRegistration(
        @ModelAttribute("user") User user) {

    service.registerUserWithRole(user, user.getRole());

    System.out.println(
            "User Registered: "
            + user.getEmail()
            + " with role: "
            + user.getRole()
    );

    return "redirect:/login";
}

@GetMapping("/access-denied")
public String accessDenied() {
    return "access-denied";
}

}
