package com.Bigproject.ecommerce_springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.Bigproject.ecommerce_springboot.Exception.InsufficientStockException;
import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.entity.Cart;
import com.Bigproject.ecommerce_springboot.entity.Product;

@Service
@SessionScope
public class CartService {

	@Autowired
	ProductRepository repo;

	private List<Cart> cartItems = new ArrayList<>();

	public void addItem(Product product, int quantity) {

		if (quantity <= 0) {
			throw new IllegalArgumentException(
					"Quantity must be greater than 0");
		}

		for (Cart item : cartItems) {

			if (item.getProduct().getId().equals(product.getId())) {

				int newQuantity = item.getQuantity() + quantity;

				if (newQuantity > product.getStock()) {
					throw new InsufficientStockException(
							"Not enough stock available for " + product.getName());
				}

				item.setQuantity(newQuantity);
				return;
			}
		}

		if (quantity > product.getStock()) {
			throw new InsufficientStockException(
					"Not enough stock available for " + product.getName());
		}

		cartItems.add(new Cart(product, quantity));
	}

	public void removeQuantity(Product product, int quantity) {

		if (quantity <= 0) {
			throw new IllegalArgumentException(
					"Quantity must be greater than 0");
		}

		for (Cart item : cartItems) {

			if (item.getProduct().getId().equals(product.getId())) {

				int newQuantity = item.getQuantity() - quantity;

				if (newQuantity <= 0) {
					cartItems.remove(item);
				} else {
					item.setQuantity(newQuantity);
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

	public String updateQuantity(Product product, int quantity) {

		if (quantity <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than 0");
		}

		for (Cart item : cartItems) {

			if (item.getProduct().getId().equals(product.getId())) {

				if (quantity > product.getStock()) {

					item.setQuantity(product.getStock());

					return "Only " + product.getStock()
							+ " items are currently available. "
							+ "Quantity has been adjusted to available stock.";
				}

				item.setQuantity(quantity);
				return null;
			}
		}

		return null;
	}

	public void clearCart() {
		cartItems.clear();
	}

}
