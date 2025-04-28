package com.example.chart.components;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
public class OrderConfirmationListener {

    @RabbitListener(queues = "order-confirmation-queue")
    public void handleOrderConfirmation(String orderId) {
        // Send confirmation email (mock implementation)
        System.out.println("Sending confirmation email for order: " + orderId);
    }
}
