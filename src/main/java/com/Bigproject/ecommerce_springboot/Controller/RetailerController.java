package com.Bigproject.ecommerce_springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.entity.Product;
import com.Bigproject.ecommerce_springboot.service.ProductService;

@Controller
@RequestMapping("/retailer")
public class RetailerController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping("/dashboard")
    public String retailerDashboard() {
        return "retailer-dashboard";
    }
    
    @GetMapping("/products")
    public String viewMyProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "retailer-products";
    }
    
    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }
    
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.savethis(product);
        return "redirect:/retailer/products";
    }
    
    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepository.findById(id).orElse(new Product()));
        return "product-form";
    }
    
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deletethisId(id);
        return "redirect:/retailer/products";
    }
    
    @GetMapping("/orders")
    public String viewOrders() {
        return "retailer-orders";
    }
}
