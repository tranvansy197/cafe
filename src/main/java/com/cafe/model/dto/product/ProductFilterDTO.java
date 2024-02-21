package com.cafe.model.dto.product;

import com.cafe.server.helper.criteria.Filter;
import lombok.Data;

@Data
public class ProductFilterDTO {
    Filter productId;
    Filter productName;
    Filter description;
    Filter price;
    Filter totalQuantity;
}
