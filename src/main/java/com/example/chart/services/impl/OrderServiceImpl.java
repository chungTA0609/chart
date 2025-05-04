package com.example.chart.services.impl;

import com.example.chart.dto.OrderDTO;
import com.example.chart.dto.OrderItemDTO;
import com.example.chart.models.*;
import com.example.chart.repository.OrderRepository;
import com.example.chart.repository.ProductRepository;
import com.example.chart.repository.UserRepository;
import com.example.chart.services.NotificationService;
import com.example.chart.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, OrderDTO orderDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setBillingAddress(orderDTO.getBillingAddress());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setActive(true);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            item.setActive(true);

            order.getItems().add(item);
            totalAmount = totalAmount.add(item.getSubtotal());
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        // Send notification
        notificationService.createOrderNotification(
            userId,
            savedOrder.getId(),
            NotificationType.ORDER_STATUS_CHANGE,
            "Your order has been created and is pending payment"
        );

        return mapOrderToDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return mapOrderToDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdAndActiveTrue(userId, pageable)
                .map(this::mapOrderToDTO);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setStatus(status);
        if (status == OrderStatus.DELIVERED) {
            order.setDeliveryDate(LocalDateTime.now());
        }

        Order savedOrder = orderRepository.save(order);

        // Send notification
        notificationService.createOrderNotification(
            order.getUser().getId(),
            id,
            NotificationType.ORDER_STATUS_CHANGE,
            "Your order status has been updated to: " + status
        );

        return mapOrderToDTO(savedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updateTrackingNumber(Long id, String trackingNumber) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setTrackingNumber(trackingNumber);
        Order savedOrder = orderRepository.save(order);

        // Send notification
        notificationService.createOrderNotification(
            order.getUser().getId(),
            id,
            NotificationType.ORDER_SHIPPED,
            "Your order has been shipped. Tracking number: " + trackingNumber
        );

        return mapOrderToDTO(savedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // Send notification
        notificationService.createOrderNotification(
            order.getUser().getId(),
            id,
            NotificationType.ORDER_STATUS_CHANGE,
            "Your order has been cancelled"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatusAndActiveTrue(status, pageable)
                .map(this::mapOrderToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return orderRepository.findByOrderDateBetweenAndActiveTrue(startDate, endDate, pageable)
                .map(this::mapOrderToDTO);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderAddresses(Long id, String shippingAddress, String billingAddress) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Addresses can only be updated for pending orders");
        }

        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
        return mapOrderToDTO(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderDTO updatePaymentMethod(Long id, String paymentMethod) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Payment method can only be updated for pending orders");
        }

        order.setPaymentMethod(paymentMethod);
        return mapOrderToDTO(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void processOrderPayment(Long id) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be processed");
        }

        // Here you would integrate with your payment gateway
        // For now, we'll just update the status
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        // Send notification
        notificationService.createOrderNotification(
            order.getUser().getId(),
            id,
            NotificationType.ORDER_STATUS_CHANGE,
            "Your order payment has been processed successfully"
        );
    }

    @Override
    @Transactional
    public void markOrderAsShipped(Long id, String trackingNumber) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Only processing orders can be marked as shipped");
        }

        order.setStatus(OrderStatus.SHIPPED);
        order.setTrackingNumber(trackingNumber);
        orderRepository.save(order);

        // Send notification
        notificationService.createOrderNotification(
            order.getUser().getId(),
            id,
            NotificationType.ORDER_SHIPPED,
            "Your order has been shipped. Tracking number: " + trackingNumber
        );
    }

    @Override
    @Transactional
    public void markOrderAsDelivered(Long id) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Only shipped orders can be marked as delivered");
        }

        order.setStatus(OrderStatus.DELIVERED);
        order.setDeliveryDate(LocalDateTime.now());
        orderRepository.save(order);

        // Send notification
        notificationService.createOrderNotification(
            order.getUser().getId(),
            id,
            NotificationType.ORDER_DELIVERED,
            "Your order has been delivered"
        );
    }

    @Override
    @Transactional
    public void refundOrder(Long id) {
        Order order = orderRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Only delivered orders can be refunded");
        }

        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);

        // Send notification
        notificationService.createOrderNotification(
            order.getUser().getId(),
            id,
            NotificationType.ORDER_STATUS_CHANGE,
            "Your order has been refunded"
        );
    }

    private OrderDTO mapOrderToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setItems(order.getItems().stream()
                .map(this::mapOrderItemToDTO)
                .collect(Collectors.toSet()));
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setBillingAddress(order.getBillingAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }

    private OrderItemDTO mapOrderItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
} 