package com.cafe.model.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class PaginationDTO {
    @Builder.Default
    private Integer limit = 10;
    @Builder.Default
    private Long offset = 0L;
    private Long total;
}
