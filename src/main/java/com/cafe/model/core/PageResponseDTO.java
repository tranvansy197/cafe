package com.cafe.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class PageResponseDTO<T> {
    List<T> items;
    PaginationDTO pagination;
}
