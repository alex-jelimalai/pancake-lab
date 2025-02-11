package com.pancake.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @Setter
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
    @Column(name = "details")
    private String details;

    @Setter
    @Column(name = "price")
    private double price;

    @Builder
    public Product(String name, String ingridients, String details, double price) {
        this.name = name;
        this.ingridients = ingridients;
        this.details = details;
        this.price = price;
    }
}
