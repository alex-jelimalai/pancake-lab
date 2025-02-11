package com.pancake.service;

import com.pancake.dto.ProductDto;
import com.pancake.model.Product;
import com.pancake.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ConversionService conversionService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
        product = new Product("Pancake", "Flour, Sugar, Milk", "Delicious pancake", 5.99);
        product.setId(1L);

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Pancake");
        productDto.setIngridients("Flour, Sugar, Milk");
        productDto.setDetails("Delicious pancake");
        productDto.setPrice(5.99);
    }

    @Test
    void findByTerm_ShouldReturnProductDtos_WhenFilterTermIsNullOrEmpty() {

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(conversionService.convert(product, ProductDto.class)).thenReturn(productDto);

        List<ProductDto> result = productService.findByTerm(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productDto.getName(), result.get(0).getName());
        verify(productRepository).findAll();
    }

    @Test
    void findByTerm_ShouldReturnProductDtos_WhenFilterTermIsProvided() {

        String filterTerm = "Pancake";
        when(productRepository.searchByName(filterTerm)).thenReturn(List.of(product));
        when(conversionService.convert(product, ProductDto.class)).thenReturn(productDto);

        List<ProductDto> result = productService.findByTerm(filterTerm);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productDto.getName(), result.get(0).getName());
        verify(productRepository).searchByName(filterTerm);
    }


    @Test
    void saveProduct_ShouldUpdateExistingProduct_WhenProductDtoExists() {

        when(productRepository.findById(productDto.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(conversionService.convert(product, ProductDto.class)).thenReturn(productDto);

        ProductDto result = productService.saveProduct(productDto);

        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());
        verify(productRepository).findById(productDto.getId());
        verify(productRepository).save(any(Product.class));
    }


    @Test
    void saveProduct_ShouldCreateNewProduct_WhenProductDtoDoesNotExist() {
        
        productDto.setId(null);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(conversionService.convert(product, ProductDto.class)).thenReturn(productDto);

        ProductDto result = productService.saveProduct(productDto);

        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());
        assertEquals(productDto.getPrice(), result.getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldCallDelete_WhenProductExists() {
        when(productRepository.existsById(product.getId())).thenReturn(true);
        productService.deleteProduct(product.getId());
        verify(productRepository).deleteById(product.getId());
    }

    @Test
    void deleteProduct_ShouldNotCallDelete_WhenProductDoesNotExist() {
        when(productRepository.existsById(product.getId())).thenReturn(false);
        productService.deleteProduct(product.getId());
        verify(productRepository, never()).deleteById(product.getId());
    }
}