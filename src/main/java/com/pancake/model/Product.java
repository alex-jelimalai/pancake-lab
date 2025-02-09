package com.pancake.model;


import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "ingridients")
    private String ingridients;

    @Setter
    @Column(name = "recipe")
    private String recipe;

    @Setter
    @Column(name = "price")
    private double price;

    @Builder
    public Product(String name, String ingridients, String recipe, double price) {
        this.name = name;
        this.ingridients = ingridients;
        this.recipe = recipe;
        this.price = price;
    }
}
