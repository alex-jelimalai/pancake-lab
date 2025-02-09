package com.pancake.model;


import com.google.common.collect.ImmutableList;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EqualsAndHashCode(of = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, unique = false)
    private OrderType orderType;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private Integer building;
    private Integer roomNo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    public void addItem(OrderItem item) {
        items.add(item);
    }

    public List<OrderItem> getOrderItems() {
        return ImmutableList.copyOf(items);
    }

    @Builder
    public Order(OrderType orderType, OrderStatus status, Integer building, Integer roomNo) {
        this.orderType = orderType;
        this.status = status;
        this.building = building;
        this.roomNo = roomNo;
    }

    public void cancel() {
        status = OrderStatus.CANCELED;
    }
}
