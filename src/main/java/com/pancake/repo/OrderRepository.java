package com.pancake.repo;

import com.pancake.model.Order;
import com.pancake.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusIn(List<OrderStatus> orderStatuses);
}
