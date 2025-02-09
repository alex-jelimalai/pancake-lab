package com.pancake.service;

import com.pancake.model.Product;
import com.pancake.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> findByTerm(String filterTerm) {
        if (filterTerm == null || filterTerm.isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.searchByName(filterTerm);
    }


    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }


    @Transactional
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }
}
