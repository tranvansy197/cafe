package com.cafe.model.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {
    private Long id;
    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now();
    private Integer totalPrice;
    private Long userId;
    private Long storeId;
    private List<OrderItemDTO> orderItems;
}
