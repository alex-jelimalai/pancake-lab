package com.pancake.repo;

import com.pancake.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where name like %:filterTerm%")
    List<Product> searchByName(String filterTerm);
}
