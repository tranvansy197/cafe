package com.cafe.server.mapper;

import com.cafe.entity.Product;
import com.cafe.entity.ProductMedia;
import com.cafe.model.dto.product.ProductDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    @Mapping(target = "productId", source = "id")
    @Mapping(target = "categoryDTO" , source = "category")
    @Mapping(target = "imageUrls" , source = "productMediaList", qualifiedByName = "mapImageUrl")
    ProductDTO toDto(Product product);

    @Mapping(target = "id", source = "productId")
    @Mapping(target = "category", source = "categoryDTO")
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "id", source = "productId")
    @Mapping(target = "category", source = "categoryDTO")
    void toEntity(@MappingTarget Product product, ProductDTO productDTO);

    @Named("mapImageUrl")
    default List<String> mapImageUrl(List<ProductMedia> list) {
        return list.stream().map(ProductMedia::getImage).toList();
    }

}
