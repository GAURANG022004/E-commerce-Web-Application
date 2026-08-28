package com.Bigproject.ecommerce_springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Bigproject.ecommerce_springboot.entity.Product;
import com.Bigproject.ecommerce_springboot.entity.User;
import com.Bigproject.ecommerce_springboot.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/retailer")
public class RetailerController {

    @Autowired
    private ProductService productService;


    // =========================================================
    // RETAILER DASHBOARD
    // =========================================================

    @GetMapping("/dashboard")
    public String retailerDashboard(
            HttpSession session,
            Model model) {

        User retailer =
                (User) session.getAttribute("user");

        if (retailer == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "products",
                productService.findProductsByRetailer(
                        retailer.getUser_id()
                )
        );

        return "retailer-dashboard";
    }


    // =========================================================
    // MY PRODUCTS
    // =========================================================

    @GetMapping("/products")
    public String viewMyProducts(
            HttpSession session,
            Model model) {

        User retailer =
                (User) session.getAttribute("user");

        if (retailer == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "products",
                productService.findProductsByRetailer(
                        retailer.getUser_id()
                )
        );

        return "retailer-products";
    }


    // =========================================================
    // ADD PRODUCT FORM
    // =========================================================

    @GetMapping("/products/new")
    public String newProductForm(
            HttpSession session,
            Model model) {

        User retailer =
                (User) session.getAttribute("user");

        if (retailer == null) {
            return "redirect:/login";
        }

        model.addAttribute("product", new Product());

        return "product-form";
    }


    // =========================================================
    // SAVE PRODUCT
    // =========================================================

    @PostMapping("/products/save")
    public String saveProduct(
            @ModelAttribute Product product,
            HttpSession session) {

        User retailer =
                (User) session.getAttribute("user");

        if (retailer == null) {
            return "redirect:/login";
        }

        // Attach logged-in retailer to product
        productService.saveProductForRetailer(
                product,
                retailer
        );

        return "redirect:/retailer/products";
    }


    // =========================================================
    // EDIT PRODUCT
    // =========================================================

    @GetMapping("/products/edit/{id}")
    public String editProduct(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        User retailer =
                (User) session.getAttribute("user");

        if (retailer == null) {
            return "redirect:/login";
        }

        Product product =
                productService.findRetailerProduct(
                        id,
                        retailer.getUser_id()
                );

        // Product does not belong to this retailer
        if (product == null) {
            return "redirect:/retailer/products";
        }

        model.addAttribute("product", product);

        return "product-form";
    }


    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            HttpSession session) {

        User retailer =
                (User) session.getAttribute("user");

        if (retailer == null) {
            return "redirect:/login";
        }

        productService.deleteRetailerProduct(
                id,
                retailer.getUser_id()
        );

        return "redirect:/retailer/products";
    }


    // =========================================================
    // RETAILER ORDERS
    // =========================================================

    @GetMapping("/orders")
    public String viewOrders(
            HttpSession session) {

        User retailer =
                (User) session.getAttribute("user");

        if (retailer == null) {
            return "redirect:/login";
        }

        return "retailer-orders";
    }
}