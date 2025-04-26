package com.example.chart.services;

import com.example.chart.dto.orders.OrderItemDTO;
import com.example.chart.dto.orders.OrderSummaryDTO;
import com.example.chart.models.EmbeddableAddress;
import com.example.chart.models.Order;
import com.example.chart.models.OrderStatus;
import com.example.chart.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;

    private RabbitTemplate rabbitTemplate;

    @Transactional
    public Order placeOrder(Order order, String paymentMethod) {
        // Validate stock, address, payment (mock implementation)
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Ensure shippingAddress is properly set (mock conversion from Address entity if needed)
        if (order.getShippingAddress() == null) {
            EmbeddableAddress address = new EmbeddableAddress();
            // Populate address fields (in real implementation, map from Address entity)
            order.setShippingAddress(address);
        }

        // Process payment (mock implementation)
        if ("CARD".equals(paymentMethod)) {
            // Integrate with Stripe/PayPal
            order.setStatus(OrderStatus.PAID);
        } else if ("COD".equals(paymentMethod)) {
            order.setStatus(OrderStatus.PAID);
        }

        Order savedOrder = orderRepository.save(order);

        // Send order confirmation to RabbitMQ
        rabbitTemplate.convertAndSend("order-confirmation-queue", savedOrder.getOrderNumber());

        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatus(UUID orderId, OrderStatus newStatus, String trackingNumber) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Validate status transition
        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new RuntimeException("Invalid status transition from " + order.getStatus() + " to " + newStatus);
        }

        order.setStatus(newStatus);
        if (trackingNumber != null && newStatus == OrderStatus.SHIPPED) {
            order.setTrackingNumber(trackingNumber);
        }
        order.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);

        // Send status update notification to RabbitMQ
        rabbitTemplate.convertAndSend("order-confirmation-queue",
                "Order " + updatedOrder.getOrderNumber() + " updated to " + newStatus);

        return updatedOrder;
    }

    public Order getOrderDetails(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Page<Order> getOrderHistory(
            UUID userId,
            int page,
            int size,
            String status,
            LocalDateTime dateFrom,
            LocalDateTime dateTo,
            String sortBy,
            String sortDirection) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        if (status != null && !status.isEmpty()) {
            return orderRepository.findByUserIdAndStatus(userId, OrderStatus.valueOf(status), pageRequest);
        } else if (dateFrom != null && dateTo != null) {
            return orderRepository.findByUserIdAndCreatedAtBetween(userId, dateFrom, dateTo, pageRequest);
        } else {
            return orderRepository.findByUserId(userId, pageRequest);
        }
    }

    private String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%04d", new Random().nextInt(10000));
        return "ORD-" + datePart + "-" + randomPart;
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case PENDING -> next == OrderStatus.PAID;
            case PAID -> next == OrderStatus.SHIPPED;
            case SHIPPED -> next == OrderStatus.DELIVERED;
            case DELIVERED -> next == OrderStatus.COMPLETED;
            default -> false;
        };
    }
}
