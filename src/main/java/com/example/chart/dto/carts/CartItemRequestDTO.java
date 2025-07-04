package com.example.chart.dto.carts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDTO {
    @NotNull
    private Long productId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
