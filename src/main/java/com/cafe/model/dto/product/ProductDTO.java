package com.cafe.model.dto.product;

import com.cafe.model.dto.category.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String name;
    private String description;
    private Integer price;
    private Integer totalQuantity;
    private CategoryDTO categoryDTO;
    private List<String> imageUrls;
}
