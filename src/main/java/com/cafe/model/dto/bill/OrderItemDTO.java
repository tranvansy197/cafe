package com.cafe.model.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDTO {
    private Long id;
    @Builder.Default
    private Boolean isEnable = false;
    private Integer quantity;
    private Long productId;
    private Long orderId;
    private Long storeId;
}
