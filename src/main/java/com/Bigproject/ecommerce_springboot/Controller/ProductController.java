package com.Bigproject.ecommerce_springboot.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.Repository.UserRepository;
import com.Bigproject.ecommerce_springboot.entity.Product;
import com.Bigproject.ecommerce_springboot.entity.User;
import com.Bigproject.ecommerce_springboot.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	ProductService service;

	@Autowired
	ProductRepository repo;

	@Autowired
	UserRepository userRepository;

	@ModelAttribute
	public void addCatalogContext(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
				.anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
		boolean isRetailer = authentication != null && authentication.getAuthorities().stream()
				.anyMatch(authority -> "ROLE_RETAILER".equals(authority.getAuthority()));

		model.addAttribute("isStaff", isAdmin || isRetailer);
		model.addAttribute("catalogBackUrl", isAdmin ? "/admin/dashboard"
				: (isRetailer ? "/retailer/dashboard" : "/customer/dashboard"));
		model.addAttribute("catalogBackLabel", isAdmin ? "Admin Dashboard"
				: (isRetailer ? "Retailer Dashboard" : "Shop Home"));
	}

	// Home Page
	
	// show add form
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("formAction", "/products/save");
		model.addAttribute("cancelUrl", "/products");
		return "product-form";
	}

	// save product
	@PostMapping("/products/save")
	public String save(@ModelAttribute Product prod) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getAuthorities().stream()
				.anyMatch(authority -> "ROLE_RETAILER".equals(authority.getAuthority()))) {
			User retailer = userRepository.findByEmail(authentication.getName());
			if (retailer == null || !service.saveProductForRetailer(prod, retailer)) {
				return "redirect:/retailer/products";
			}
			return "redirect:/retailer/products";
		}
		service.savethis(prod);
		return "redirect:/products";
	}

	// Edit product
	@GetMapping("/products/edit/{id}")
	public String updateProduct(@PathVariable Long id, Model model) {

		Product product = repo.findById(id).orElse(new Product());
		Long retailerId = currentRetailerId();
		if (retailerId != null) {
			product = service.findRetailerProduct(id, retailerId);
			if (product == null) {
				return "redirect:/products";
			}
		}
		model.addAttribute("product", product);
		model.addAttribute("formAction", "/products/save");
		model.addAttribute("cancelUrl", "/products");
		return "product-form";
	}

//	// Edit product
//    @GetMapping("/products/edit/{id}")
//    public String editProduct(@PathVariable Long id, Model model) {
//        Product product = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
//        model.addAttribute("product", product);
//        return "product-form";
//    }

	// Delete Product
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		System.out.println("You are requesting id :" + id + " for delete");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getAuthorities().stream()
				.anyMatch(authority -> "ROLE_RETAILER".equals(authority.getAuthority()))) {
			service.deleteRetailerProduct(id, userRepository.findByEmail(authentication.getName()).getUser_id());
		} else {
			service.deletethisId(id);
		}
		return "redirect:/products";
	}

	// Searching Product
	@GetMapping("/products/search")
	public String searchProducts(@RequestParam String keyword, Model model) {

		System.out.println("Seraching with : " + keyword);

		Page<Product> result = service.searchProducts(currentRetailerId(), keyword, null, null, null,
				PageRequest.of(0, 48));
		model.addAttribute("products", result.getContent());
		return "index";
	}

	@GetMapping("/products/filter")
	public String filterByCategory(@RequestParam String category, Model model) {

		if (category == null || category.isEmpty()) {
			Page<Product> products = service.searchProducts(currentRetailerId(), null, null, null, null,
					PageRequest.of(0, 48));
			model.addAttribute("products", products.getContent());
			return "index";
		} else {
			Page<Product> products = service.searchProducts(currentRetailerId(), null, category, null, null,
					PageRequest.of(0, 48));
			model.addAttribute("products", products.getContent());
			return "index";

		}

	}

	

	@GetMapping("/products")
	public String pagination(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			Model model) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Product> p = service.searchProducts(currentRetailerId(), null, null, null, null, pageable);

		model.addAttribute("products", p.getContent());
		model.addAttribute("currentPage", page); 
		model.addAttribute("totalPages", p.getTotalPages()); 
		model.addAttribute("size", size);

		return "index";
	}


	private Long currentRetailerId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getAuthorities().stream()
				.anyMatch(authority -> "ROLE_RETAILER".equals(authority.getAuthority()))) {
			return userRepository.findByEmail(authentication.getName()).getUser_id();
		}
		return null;
	}
}
