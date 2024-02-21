package com.cafe.model.dto.category;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CategoryDTO {
    private Long id;
    private String name;
}
