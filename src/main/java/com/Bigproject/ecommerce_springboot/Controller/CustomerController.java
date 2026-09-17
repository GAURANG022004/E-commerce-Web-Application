package com.Bigproject.ecommerce_springboot.Controller;

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
        return customerProducts(0, 12, null, null, null, null, model);
    }
    
    @GetMapping("/products")
    public String customerProducts(@RequestParam(defaultValue = "0") int page, 
                                   @RequestParam(defaultValue = "12") int size,
                                   @RequestParam(required = false) String category,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) Double minPrice,
                                   @RequestParam(required = false) Double maxPrice,
                                   Model model) {
        return renderProductResults(page, size, keyword, category, minPrice, maxPrice, model);
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String category,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "12") int size,
                                @RequestParam(required = false) Double minPrice,
                                @RequestParam(required = false) Double maxPrice,
                                Model model) {
        return renderProductResults(page, size, keyword, category, minPrice, maxPrice, model);
    }

    private String renderProductResults(int page, int size, String keyword, String category,
            Double minPrice, Double maxPrice, Model model) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 6), 48);
        Pageable pageable = PageRequest.of(safePage, safeSize);
        Page<Product> products = productService.searchProducts(null, keyword, category, minPrice, maxPrice, pageable);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("size", safeSize);
        model.addAttribute("cartCount", cartService.getCartItems().size());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
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
