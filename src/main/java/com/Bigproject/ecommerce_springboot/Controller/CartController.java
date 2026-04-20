package com.Bigproject.ecommerce_springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

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
		model.addAttribute("total",service.getTotal());
		
		return "cart";
	}
	
	@GetMapping("/add/{id}")
	public String addToCart(@PathVariable Long id, @RequestParam(defaultValue = "1")int quantity) {
		Product product = repo.findById(id).orElseThrow(() ->new productnotfoundexception("Product Not Found! Please Try Again"));
		service.addItem(product,quantity);
		return "redirect:/products";
		
	}
	
	@GetMapping("/cart/remove1/{id}")
	public String removeQuantity(@PathVariable Long id, @RequestParam(defaultValue = "1")int quantity) {
		Product product = repo.findById(id).orElseThrow(() ->new productnotfoundexception("Product Not Found! Please Try Again"));
		service.removeQuantity(product,quantity);
		return "redirect:/cart";
		
	}
	
	
	
	@GetMapping("/cart/remove/{id}")
	public String removeFromCart(@PathVariable Long id) {
		service.deleteById(id);
		return "redirect:/cart";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	
//	me springboot,jpa,thymeleaf,h2 ye sab milke ek project bana rha hu bass help karo logic lagane me 
//	eke ek word ka mtlb chihye mujjhe 
//
//
//	mujhe add to cart option lagana he mere ecommerce webstire ko 
//
//	@GetMapping("/cart")
//		public String showcart(Model model) {
//			
//			model.addAttribute("cartItems", service.getCartItems());
//			model.addAttribute("total",service.getTotal());
//			
//			return "cart";
//		}
//	ChatGPT said:
//
//	Perfect! Tum Spring Boot + JPA + Thymeleaf + H2 use kar rahe ho, aur Add to Cart functionality implement karna chahte ho. Chalo step by step logic samajhte hain. Main tumhare code snippet ke context me bataunga.
//
//	1️⃣ Cart ka basic idea
//
//	Cart basically ek temporary collection of products hai jo user ne select kiye hain, aur order place karne tak store hote hain.
//
//	So tumhe do cheezein manage karni hongi:
//
//	CartItem entity – har product jo cart me hai, uski details.
//
//	Cart Service – cart me add, remove aur total calculate karne ka logic.
//
//	2️⃣ CartItem entity (JPA)
//	@Entity
//	public class CartItem {
//
//	    @Id
//	    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	    private Long id;
//
//	    private String productName;
//	    private int quantity;
//	    private double price; // single item price
//
//	    // optional: calculate subtotal
//	    public double getSubtotal() {
//	        return this.quantity * this.price;
//	    }
//
//	    // getters and setters
//	}
//
//	3️⃣ CartService
//
//	Service me cart items store aur manage karte ho. Simple version me H2 memory DB me ya List<CartItem> me rakh sakte ho.
//
//	@Service
//	public class CartService {
//
//	    private List<CartItem> cartItems = new ArrayList<>();
//
//	    // Add item to cart
//	    public void addToCart(Product product, int quantity) {
//	        for (CartItem item : cartItems) {
//	            if (item.getProductName().equals(product.getName())) {
//	                item.setQuantity(item.getQuantity() + quantity);
//	                return;
//	            }
//	        }
//	        CartItem newItem = new CartItem();
//	        newItem.setProductName(product.getName());
//	        newItem.setPrice(product.getPrice());
//	        newItem.setQuantity(quantity);
//	        cartItems.add(newItem);
//	    }
//
//	    // Get all items
//	    public List<CartItem> getCartItems() {
//	        return cartItems;
//	    }
//
//	    // Get total
//	    public double getTotal() {
//	        return cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
//	    }
//
//	    // Remove item
//	    public void removeItem(Long id) {
//	        cartItems.removeIf(item -> item.getId().equals(id));
//	    }
//	}
//
//	4️⃣ Controller me Add to Cart button ka logic
//
//	Suppose tumhare product page me form hai:
//
//	@PostMapping("/add-to-cart")
//	public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
//	    Product product = productService.getProductById(productId);
//	    service.addToCart(product, quantity);
//	    return "redirect:/cart"; // redirect to cart page
//	}
//
//	5️⃣ Thymeleaf cart page
//	<table>
//	    <tr>
//	        <th>Product</th>
//	        <th>Quantity</th>
//	        <th>Price</th>
//	        <th>Subtotal</th>
//	    </tr>
//	    <tr th:each="item : ${cartItems}">
//	        <td th:text="${item.productName}">Product Name</td>
//	        <td th:text="${item.quantity}">1</td>
//	        <td th:text="${item.price}">100</td>
//	        <td th:text="${item.subtotal}">100</td>
//	    </tr>
//	</table>
//
//	<p>Total: <span th:text="${total}">0</span></p>
//
//
//	me springboot,jpa,thymeleaf,h2 ye sab milke ek project bana rha hu bass help karo logic lagane me 
//	eke ek word ka mtlb chihye mujjhe 
//
//
//	mujhe add to cart option lagana he mere ecommerce webstire ko 
//
//	@GetMapping("/cart")
//		public String showcart(Model model) {
//			
//			model.addAttribute("cartItems", service.getCartItems());
//			model.addAttribute("total",service.getTotal());
//			
//			return "cart";
//		}
//	ChatGPT said:
//
//	Perfect! Tum Spring Boot + JPA + Thymeleaf + H2 use kar rahe ho, aur Add to Cart functionality implement karna chahte ho. Chalo step by step logic samajhte hain. Main tumhare code snippet ke context me bataunga.
//
//	1️⃣ Cart ka basic idea
//
//	Cart basically ek temporary collection of products hai jo user ne select kiye hain, aur order place karne tak store hote hain.
//
//	So tumhe do cheezein manage karni hongi:
//
//	CartItem entity – har product jo cart me hai, uski details.
//
//	Cart Service – cart me add, remove aur total calculate karne ka logic.
//
//	2️⃣ CartItem entity (JPA)
//	@Entity
//	public class CartItem {
//
//	    @Id
//	    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	    private Long id;
//
//	    private String productName;
//	    private int quantity;
//	    private double price; // single item price
//
//	    // optional: calculate subtotal
//	    public double getSubtotal() {
//	        return this.quantity * this.price;
//	    }
//
//	    // getters and setters
//	}
//
//	3️⃣ CartService
//
//	Service me cart items store aur manage karte ho. Simple version me H2 memory DB me ya List<CartItem> me rakh sakte ho.
//
//	@Service
//	public class CartService {
//
//	    private List<CartItem> cartItems = new ArrayList<>();
//
//	    // Add item to cart
//	    public void addToCart(Product product, int quantity) {
//	        for (CartItem item : cartItems) {
//	            if (item.getProductName().equals(product.getName())) {
//	                item.setQuantity(item.getQuantity() + quantity);
//	                return;
//	            }
//	        }
//	        CartItem newItem = new CartItem();
//	        newItem.setProductName(product.getName());
//	        newItem.setPrice(product.getPrice());
//	        newItem.setQuantity(quantity);
//	        cartItems.add(newItem);
//	    }
//
//	    // Get all items
//	    public List<CartItem> getCartItems() {
//	        return cartItems;
//	    }
//
//	    // Get total
//	    public double getTotal() {
//	        return cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
//	    }
//
//	    // Remove item
//	    public void removeItem(Long id) {
//	        cartItems.removeIf(item -> item.getId().equals(id));
//	    }
//	}
//
//	4️⃣ Controller me Add to Cart button ka logic
//
//	Suppose tumhare product page me form hai:
//
//	@PostMapping("/add-to-cart")
//	public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
//	    Product product = productService.getProductById(productId);
//	    service.addToCart(product, quantity);
//	    return "redirect:/cart"; // redirect to cart page
//	}
//
//	5️⃣ Thymeleaf cart page
//	<table>
//	    <tr>
//	        <th>Product</th>
//	        <th>Quantity</th>
//	        <th>Price</th>
//	        <th>Subtotal</th>
//	    </tr>
//	    <tr th:each="item : ${cartItems}">
//	        <td th:text="${item.productName}">Product Name</td>
//	        <td th:text="${item.quantity}">1</td>
//	        <td th:text="${item.price}">100</td>
//	        <td th:text="${item.subtotal}">100</td>
//	    </tr>
//	</table>
//
//	<p>Total: <span th:text="${total}">0</span></p>
//
//
//	Agar chaho, mai tumhare liye complete mini working Spring Boot project structure likh ke de sakta hu jisme Products, Add to Cart, Cart page aur Total calculation sab included ho.
//
//	Kya mai ye complete structure bana du?
//
//	You said:
//	mujhe ye sab code nahi chahiye mujhe ...
//
//	mujhe bss ek line ka implementation do aur fir mujhe kuch sawal pucho jese ye hum kyu likha aisa code me?
//	aur ab iska kya use he? ya fir ab hume aisa karne he toh hum kya kya kar skte he ?
//
//	aisa pura mera enterprise level applications ke logic sikhna he 
	
	

}
