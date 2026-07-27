package com.Bigproject.ecommerce_springboot.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.entity.Product;
import com.Bigproject.ecommerce_springboot.service.CartService;
import com.Bigproject.ecommerce_springboot.service.ProductService;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/dashboard")
    public String customerDashboard(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("cartCount", cartService.getCartItems().size());
        return "customer-dashboard";
    }
    
    @GetMapping("/products")
    public String customerProducts(@RequestParam(defaultValue = "0") int page, 
                                   @RequestParam(defaultValue = "12") int size,
                                   @RequestParam(required = false) String category,
                                   Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products;
        
        if (category != null && !category.isEmpty()) {
            List<Product> categoryProducts = productService.findByCatgory(category);
            model.addAttribute("products", categoryProducts);
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 1);
        } else {
            products = productService.findAll(pageable);
            model.addAttribute("products", products.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", products.getTotalPages());
        }
        
        model.addAttribute("size", size);
        model.addAttribute("cartCount", cartService.getCartItems().size());
        model.addAttribute("selectedCategory", category);
        return "customer-dashboard";
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String category,
                                Model model) {
        List<Product> result;

        if (keyword != null && !keyword.isEmpty() && category != null && !category.isEmpty()) {
            result = productService.searchByKeywordAndCategory(keyword, category);
        } else if (keyword != null && !keyword.isEmpty()) {
            result = productService.searchByOptionalParams(keyword);
        } else if (category != null && !category.isEmpty()) {
            result = productService.findByCatgory(category);
        } else {
            result = productService.findAll();
        }

        model.addAttribute("products", result);
        model.addAttribute("cartCount", cartService.getCartItems().size());
        return "customer-dashboard";
    }
    
    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("cartCount", cartService.getCartItems().size());
        return "cart";
    }
    
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam(defaultValue = "1") int quantity) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        cartService.addItem(product, quantity);
        return "redirect:/customer/products";
    }
    
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.deleteById(id);
        return "redirect:/customer/cart";
    }
    
    @GetMapping("/cart/update/{id}")
    public String updateCartQuantity(@PathVariable Long id, @RequestParam int quantity) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        if (quantity <= 0) {
            cartService.deleteById(id);
        } else {
            cartService.updateQuantity(product, quantity);
        }
        return "redirect:/customer/cart";
    }
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotal());
        return "checkout";
    }
    
    @PostMapping("/place-order")
    public String placeOrder(Model model) {
        // Clear cart after placing order
        cartService.clearCart();
        model.addAttribute("message", "Order placed successfully!");
        return "order-success";
    }
    
    @GetMapping("/orders")
    public String viewOrders(Model model) {
        model.addAttribute("cartCount", cartService.getCartItems().size());
        return "orders";
    }
    
    @GetMapping("/profile")
    public String viewProfile(Model model) {
        model.addAttribute("cartCount", cartService.getCartItems().size());
        return "customer-profile";
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
