package com.Bigproject.ecommerce_springboot.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.entity.Product;
import com.Bigproject.ecommerce_springboot.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	ProductService service;

	@Autowired
	ProductRepository repo;

	// Home Page
	
	// show add form
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		model.addAttribute("product", new Product());
		return "product-form";
	}

	// save product
	@PostMapping("/products/save")
	public String save(@ModelAttribute Product prod) {
		service.savethis(prod);
		return "redirect:/products";
	}

	// Edit product
	@GetMapping("/products/edit/{id}")
	public String updateProduct(@PathVariable Long id, Model model) {

		model.addAttribute("product", repo.findById(id).orElse(new Product()));
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
		service.deletethisId(id);
		return "redirect:/products";
	}

	// Searching Product
	@GetMapping("/products/search")
	public String searchProducts(@RequestParam String keyword, Model model) {

		System.out.println("Seraching with : " + keyword);

		List<Product> result = service.searchByOptionalParams(keyword);
		model.addAttribute("products", result);
		return "index";
	}

	@GetMapping("/products/filter")
	public String filterByCategory(@RequestParam String category, Model model) {

		if (category == null || category.isEmpty()) {
			List<Product> products = service.findAll();
			model.addAttribute("products", products);
			return "index";
		} else {
			List<Product> products = service.findByCatgory(category);
			model.addAttribute("products", products);
			return "index";

		}

	}

	@GetMapping("/products/cart")
	public Product cart(Product product) {

		List<Product> cart = service.addInCart(product);
		return product;

	}

	@GetMapping("/products")
	public String pagination(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			Model model) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Product> p = service.findAll(pageable);

		model.addAttribute("products", p.getContent());
		model.addAttribute("currentPage", page); 
		model.addAttribute("totalPages", p.getTotalPages()); 
		model.addAttribute("size", size);

		return "index";
	}

}
