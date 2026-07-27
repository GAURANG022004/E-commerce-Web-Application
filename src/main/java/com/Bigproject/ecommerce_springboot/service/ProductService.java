package com.Bigproject.ecommerce_springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Bigproject.ecommerce_springboot.Repository.ProductRepository;
import com.Bigproject.ecommerce_springboot.entity.Product;

@Service
public class ProductService {
	
	@Autowired
	ProductRepository repo;

	
	public List<Product> AllProduct() {
		return repo.findAll();
	}


	//Saving....
	public void savethis(Product product) {
		repo.save(product);
	}


	//edit Product
//	public Product updateProd(Long id) {
//		
//        return repo.findById(id).orElse(new Product());
//	}
	
	

	//Delete Product
	public void deletethisId(Long id) {
		repo.deleteById(id);
	}


	public Product updateProd(Long id) {
		return repo.findById(id).orElse(new Product());
		
	}

//
//	public List<Product> findbyname(String name) {
//		
//		return repo.findByNameContainingIgnoreCase(name);
//		
//	}
//
//
//	public List<Product> searchbythis(String name, String category, String description) {
//		
//		return repo.findByNameContainingIgnoreCaseAndCategoryIgnoreCaseAndDescriptionIgnoreCase(name, category, description);
//			
//	}
//
//
//	public List<Product> findbycatg(String category) {
//		return repo.findByCategoryIgnoreCase(category);
//		
//	}
//
//
//	public List<Product> findbydesc(String description) {
//		return repo.findByDescriptionIgnoreCase(description);
//		
//	}


	public List<Product> findAll() {
		return repo.findAll();
	}


	public List<Product> searchByOptionalParams(String keyword) {

		

		return repo.searchByOptionalParams(keyword);
		
	}

	public List<Product> findByCatgory(String category){
		
		
		System.out.println("Category Applied...");
		return repo.findByCategoryIgnoreCase(category);
		
	}


	public List<Product> addInCart(Product product) {
		
		return null;
	}


	public Page<Product> findAll(Pageable pageable) {
		
		return repo.findAll(pageable);
		
	}

    public List<Product> searchByKeywordAndCategory(String keyword, String category) {
        
        return repo.searchByKeywordAndCategory(keyword, category);
    }





	





}
