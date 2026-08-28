package com.Bigproject.ecommerce_springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.entity.Product;
import com.Bigproject.ecommerce_springboot.entity.User;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;


    public List<Product> AllProduct() {
        return repo.findAll();
    }


    public void savethis(Product product) {
        repo.save(product);
    }


    public void deletethisId(Long id) {
        repo.deleteById(id);
    }


    public Product updateProd(Long id) {
        return repo.findById(id).orElse(new Product());
    }


    public List<Product> findAll() {
        return repo.findAll();
    }


    public Page<Product> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }


    public List<Product> searchByOptionalParams(String keyword) {
        return repo.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword,
                keyword
        );
    }


    public List<Product> findByCatgory(String category) {
        return repo.findByCategoryIgnoreCase(category);
    }


    public List<Product> searchByKeywordAndCategory(
            String keyword,
            String category) {

        return repo.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(
                keyword,
                category
        );
    }


    // =========================================================
    // RETAILER PRODUCT METHODS
    // =========================================================

	//ONLY RETAILER CAN VIEW THEIR OWN PRODUCTS
    public List<Product> findProductsByRetailer(Long retailerId) {
        return repo.findByRetailerUser_id(retailerId);
    }

	//ONLY RETAILER CAN VIEW THEIR OWN PRODUCTS
    public Product findRetailerProduct(Long productId, Long retailerId) {

        return repo.findByIdAndRetailerUser_id(
                productId,
                retailerId
        );
    }

	//ONLY RETAILER CAN ADD PRODUCTS
    public void saveProductForRetailer(Product product, User retailer) {

        product.setRetailer(retailer);

        repo.save(product);
    }

	//ONLY RETAILER CAN DELETE THEIR OWN PRODUCTS
    public boolean deleteRetailerProduct(
            Long productId,
            Long retailerId) {

        Product product =
                repo.findByIdAndRetailerUser_id(
                        productId,
                        retailerId
                );

        if (product == null) {
            return false;
        }

        repo.delete(product);

        return true;
    }
}