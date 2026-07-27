package com.Bigproject.ecommerce_springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.entity.Cart;
import com.Bigproject.ecommerce_springboot.entity.Product;

@Service
public class CartService {

	@Autowired
	ProductRepository repo;

	private List<Cart> cartItems = new ArrayList<>();

	public void addItem(Product product, int quantity) {

		for (Cart item : cartItems) {

			if (item.getProduct().getId().equals(product.getId())) {
				item.setQuantity(item.getQuantity() + quantity);
				return;
			}

		}
		cartItems.add(new Cart(product, quantity));

	}

	public void removeQuantity(Product product, int quantity) {

		for (Cart item : cartItems) {

			if (item.getProduct().getId().equals(product.getId())) {
				if (item.getQuantity() - quantity >= 0) {
					item.setQuantity(item.getQuantity() - quantity);
				}else {
					item.setQuantity(0);
				}

				return;
			}

		}

	}

	public List<Cart> getCartItems() {
		return cartItems;
	}

	public double getTotal() {
		double total = 0.0;

		for (Cart item : cartItems) {
			total += item.getTotalPrice();
		}

		return total;
	}

	public void deleteById(Long id) {
		cartItems.removeIf(item -> item.getProduct().getId().equals(id));

	}
	
	public void updateQuantity(Product product, int quantity) {
		for (Cart item : cartItems) {
			if (item.getProduct().getId().equals(product.getId())) {
				item.setQuantity(quantity);
				return;
			}
		}
	}
	
	public void clearCart() {
		cartItems.clear();
	}

}
