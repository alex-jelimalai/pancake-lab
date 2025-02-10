package com.pancake.service;

import com.pancake.dto.ProductDto;
import com.pancake.model.Product;
import com.pancake.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ConversionService conversionService;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductDto> findByTerm(String filterTerm) {
        List<Product> result;
        if (filterTerm == null || filterTerm.isEmpty()) {
            result = productRepository.findAll();
        } else {
            result = productRepository.searchByName(filterTerm);
        }
        System.out.println(result.stream().map(s -> s.getId()).collect(Collectors.toList()));
        return result.stream().map(product -> conversionService.convert(product, ProductDto.class)).collect(Collectors.toList());
    }


    @Transactional
    public ProductDto saveProduct(ProductDto productDto) {
        Product product;
        if (productDto.getId() == null) {
            product = new Product();
        } else {
            product = productRepository.findById(productDto.getId()).orElseThrow();
        }
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDetails(productDto.getDetails());
        product.setIngridients(productDto.getIngridients());
        return conversionService.convert(product, ProductDto.class);
    }


    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
