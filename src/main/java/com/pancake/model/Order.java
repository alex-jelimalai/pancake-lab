package com.pancake.model;


import com.google.common.collect.ImmutableList;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Setter
    private Integer building;
    @Setter
    private Integer roomNo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItem> items = new ArrayList<>();

    public List<OrderItem> getOrderItems() {
        return ImmutableList.copyOf(items);
    }

    public void setOrderItems(List<OrderItem> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    @Builder
    public Order(OrderType orderType, OrderStatus status, Integer building, Integer roomNo) {
        this.orderType = orderType;
        this.status = status;
        this.building = building;
        this.roomNo = roomNo;
    }

}
