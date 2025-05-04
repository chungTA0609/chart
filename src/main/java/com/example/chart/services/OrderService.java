package com.example.chart.services;

import com.example.chart.dto.OrderDTO;
import com.example.chart.models.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderService {
    OrderDTO createOrder(Long userId, OrderDTO orderDTO);
    OrderDTO getOrderById(Long id);
    Page<OrderDTO> getUserOrders(Long userId, Pageable pageable);
    OrderDTO updateOrderStatus(Long id, OrderStatus status);
    OrderDTO updateTrackingNumber(Long id, String trackingNumber);
    void cancelOrder(Long id);
    Page<OrderDTO> getOrdersByStatus(OrderStatus status, Pageable pageable);
    Page<OrderDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    OrderDTO updateOrderAddresses(Long id, String shippingAddress, String billingAddress);
    OrderDTO updatePaymentMethod(Long id, String paymentMethod);
    void processOrderPayment(Long id);
    void markOrderAsShipped(Long id, String trackingNumber);
    void markOrderAsDelivered(Long id);
    void refundOrder(Long id);
}
