package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.List;

public class OrderLog {

    public static void logAddPancake(Order order, String description, List<PancakeRecipe> pancakes) {
        long pancakesInOrder = pancakes.stream().filter(p -> p.getOrderId().equals(order.getId())).count();
        System.out.printf("Added pancake with description '%s' %n", description);
        System.out.printf("to order %s containing %d pancakes, %n", order.getId(), pancakesInOrder);
        System.out.printf("for building %d, room %d.%n", order.getBuilding(), order.getRoom());
    }

    public static void logRemovePancakes(Order order, String description, int count, List<PancakeRecipe> pancakes) {
        long pancakesInOrder = pancakes.stream().filter(p -> p.getOrderId().equals(order.getId())).count();

        System.out.printf("Removed %d pancake(s) with description '%s' ", count, description);
        System.out.printf("from order %s now containing %d pancakes, ", order.getId(), pancakesInOrder);
        System.out.printf("for building %d, room %d.", order.getBuilding(), order.getRoom());
    }

    public static void logCancelOrder(Order order, List<PancakeRecipe> pancakes) {
        long pancakesInOrder = pancakes.stream().filter(p -> p.getOrderId().equals(order.getId())).count();
        System.out.printf("Cancelled order %s with %d pancakes ", order.getId(), pancakesInOrder);
        System.out.printf("for building %d, room %d.", order.getBuilding(), order.getRoom());
    }

    public static void logDeliverOrder(Order order, List<PancakeRecipe> pancakes) {
        long pancakesInOrder = pancakes.stream().filter(p -> p.getOrderId().equals(order.getId())).count();
        System.out.printf("Order %s with %d pancakes ", order.getId(), pancakesInOrder);
        System.out.printf("for building %d, room %d out for delivery.", order.getBuilding(), order.getRoom());
    }
}
