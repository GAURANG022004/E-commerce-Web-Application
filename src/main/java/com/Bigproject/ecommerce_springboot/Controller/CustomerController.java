package com.Bigproject.ecommerce_springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.entity.Product;
import com.Bigproject.ecommerce_springboot.service.CartService;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping("/dashboard")
    public String customerDashboard() {
        return "customer-dashboard";
    }
    
    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotal());
        return "cart";
    }
    
    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam(defaultValue = "1") int quantity) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        cartService.addItem(product, quantity);
        return "redirect:/products";
    }
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotal());
        return "checkout";
    }
    
    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }
    
    @GetMapping("/orders")
    public String viewOrders() {
        return "orders";
    }
}
