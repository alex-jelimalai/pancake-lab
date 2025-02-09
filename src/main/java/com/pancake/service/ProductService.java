package com.pancake.service;

import com.pancake.model.Product;
import com.pancake.repo.ProductRepository;
import com.pancake.request.AddProductRequest;
import com.pancake.request.UpdateProductRequest;
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
        if(filterTerm == null || filterTerm.isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.searchByName(filterTerm);
    }

    @Transactional
    public Product addProduct(AddProductRequest product) {
        return productRepository.save(Product.builder()
                .name(product.getName())
                .price(product.getPrice())
                .ingridients(product.getIngridients())
                .recipe(product.getRecipe())
                .build());
    }

    @Transactional
    public Product updateProduct(UpdateProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getId()).orElseThrow(() -> new RuntimeException("Product could not be found"));
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setIngridients(productRequest.getIngridients());
        product.setRecipe(productRequest.getRecipe());
        return productRepository.save(product);
    }
}
