package com.Bigproject.ecommerce_springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Bigproject.ecommerce_springboot.productnotfoundexception;
import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.entity.Product;
import com.Bigproject.ecommerce_springboot.service.CartService;

@Controller
public class CartController {

	@Autowired
	CartService service;

	@Autowired
	ProductRepository repo;

	@GetMapping("/cart")
	public String showcart(Model model) {

		model.addAttribute("cartItems", service.getCartItems());
		model.addAttribute("total", service.getTotal());

		return "cart";
	}

	@GetMapping("/add/{id}")
	public String addToCart(@PathVariable Long id, @RequestParam(defaultValue = "1") int quantity) {
		Product product = repo.findById(id)
				.orElseThrow(() -> new productnotfoundexception("Product Not Found! Please Try Again"));
		service.addItem(product, quantity);
		return "redirect:/products";

	}

	@GetMapping("/cart/update/{id}")
	public String updateQuantity(
			@PathVariable Long id,
			@RequestParam int quantity,
			RedirectAttributes redirectAttributes) {

		Product product = repo.findById(id)
				.orElseThrow(() -> new productnotfoundexception("Product Not Found! Please Try Again"));

		String message = service.updateQuantity(product, quantity);

		if (message != null) {
			redirectAttributes.addFlashAttribute("stockMessage", message);
		}

		return "redirect:/cart";
	}

	@GetMapping("/cart/remove1/{id}")
	public String removeQuantity(@PathVariable Long id, @RequestParam(defaultValue = "1") int quantity) {
		Product product = repo.findById(id)
				.orElseThrow(() -> new productnotfoundexception("Product Not Found! Please Try Again"));
		service.removeQuantity(product, quantity);
		return "redirect:/cart";

	}

	@GetMapping("/cart/remove/{id}")
	public String removeFromCart(@PathVariable Long id) {
		service.deleteById(id);
		return "redirect:/cart";
	}

}