package com.cafe.server.service.impl;

import com.cafe.entity.Product;
import com.cafe.entity.ProductMedia;
import com.cafe.entity.Product_;
import com.cafe.model.core.OffsetLimitPageable;
import com.cafe.model.core.PageResponseDTO;
import com.cafe.model.core.PaginationDTO;
import com.cafe.model.core.SortDTO;
import com.cafe.model.dto.product.ProductDTO;
import com.cafe.model.dto.product.ProductFilterDTO;
import com.cafe.server.exception.DataNotFoundException;
import com.cafe.server.exception.GeneralException;
import com.cafe.server.exception.StorageException;
import com.cafe.server.helper.criteria.FilterField;
import com.cafe.server.helper.ultis.FileUtils;
import com.cafe.server.helper.ultis.ObjectMapperUtils;
import com.cafe.server.helper.ultis.PaginationUtils;
import com.cafe.server.mapper.ProductMapper;
import com.cafe.server.repository.ProductRepository;
import com.cafe.server.service.ProductService;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static com.cafe.model.constant.Operator.*;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private static final Map<String, String> FIELD_MAPPING = new HashMap<>();

    static {
        FIELD_MAPPING.put("productId", Product_.ID);
        FIELD_MAPPING.put("productName", Product_.NAME);
        FIELD_MAPPING.put("description", Product_.DESCRIPTION);
        FIELD_MAPPING.put("price", Product_.PRICE);
        FIELD_MAPPING.put("totalQuantity", Product_.TOTAL_QUANTITY);
    }

    @Override
    public PageResponseDTO<ProductDTO> getAllProducts(String strPagination, String strFilter, String strSort) {
        PaginationDTO paginationDTO = PaginationUtils.createPagination(strPagination);
        OffsetLimitPageable pageable = createPageable(paginationDTO, strSort);
        Page<Product> productPage = productRepository.findAll(createSpecification(strFilter), pageable);
        Page<ProductDTO> page = productPage.map(productMapper::toDto);
        return new PageResponseDTO<>(page.getContent(),
                new PaginationDTO(page.getSize(), page.getPageable().getOffset(), page.getTotalElements()));
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productMapper.toDto(getSingleEntity(id));
    }

    @Override
    @Transactional
    public void createProduct(String strProduct, MultipartFile[] files) {
        ProductDTO productDTO = strProduct != null ? ObjectMapperUtils.toObject(strProduct, ProductDTO.class) : new ProductDTO();
        Product product = productMapper.toEntity(productDTO);

        saveProduct(files, product);
    }

    @Override
    @Transactional
    public void updateProduct(Long id, String strProduct, MultipartFile[] files) {
        ProductDTO productDTO = strProduct != null ? ObjectMapperUtils.toObject(strProduct, ProductDTO.class) : new ProductDTO();
        Product product = getSingleEntity(id);
        productMapper.toEntity(product, productDTO);
        saveProduct(files, product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getSingleEntity(id);
        product.setIsDelete(true);
        productRepository.save(product);
    }

    private void saveProduct(MultipartFile[] files, Product product) {
        List<ProductMedia> productMediaList = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                productMediaList.add(ProductMedia.builder().image(FileUtils.storeFile(file)).product(product).build());
            }
        } catch (StorageException e) {
            throw new StorageException(e);
        }
        product.setProductMediaList(productMediaList);
        productRepository.save(product);
    }

    private Product getSingleEntity(Long id) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            var idPredicate = criteriaBuilder.equal(root.get(Product_.ID), id);
            return criteriaBuilder.and(idPredicate);
        };
        return productRepository.findOne(spec.and(defaultSpec())).orElseThrow(() -> createNotFoundException(id));
    }

    private DataNotFoundException createNotFoundException(Long id) {
        return new DataNotFoundException("Resource " + id + " not found ");
    }

    private OffsetLimitPageable createPageable(PaginationDTO paginationDTO, String strSort) {
        SortDTO sortDTO = PaginationUtils.createAndUpdateFieldSortIfDifferent(strSort, FIELD_MAPPING);
        return PaginationUtils.createOffsetLimitPageable(paginationDTO, sortDTO);
    }

    private Specification<Product> createSpecification(String strFilter) {
        ProductFilterDTO filters = StringUtils.isEmpty(strFilter) ? null : ObjectMapperUtils.toObject(strFilter, ProductFilterDTO.class);
        Specification<Product> spec = defaultSpec();
        if (filters != null) {
            FilterField[] filterFields = {
                    new FilterField(Product_.ID, filters.getProductId()),
                    new FilterField(Product_.NAME, filters.getProductName()),
                    new FilterField(Product_.DESCRIPTION, filters.getDescription()),
                    new FilterField(Product_.PRICE, filters.getPrice()),
                    new FilterField(Product_.TOTAL_QUANTITY, filters.getTotalQuantity())
            };
            spec.and(toSpecification(filterFields));
        }
        return spec;
    }

    private Specification<Product> toSpecification(FilterField[] filterFields) {
        Specification<Product> spec = Specification.where(null);
        for (FilterField filterField : filterFields) {
            spec.and(createSpecificationByFilter(filterField));
        }
        return spec;
    }

    private Specification<Product> createSpecificationByFilter(FilterField input) {
        return switch (toOperator(input.filter().getMatchMode())) {
            case GREATER_THAN_OR_EQUAL, GTE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(input.fieldName()),
                            input.filter().getValue().toString());
            case LESS_THAN_OR_EQUAL, LTE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(input.fieldName()),
                            input.filter().getValue().toString());
            case LIKE, CONTAINS -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(input.fieldName()),
                            "%" + input.filter().getValue().toString() + "%");
            case EQUAL -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(input.fieldName()),
                            input.filter().getValue().toString());
            case IS_NOT, NOT_EQUALS -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.notEqual(root.get(input.fieldName()),
                            input.filter().getValue().toString());
            case IN -> (root, query, criteriaBuilder) -> {
                String[] values = input.filter().getValue().toString().split(",");
                Path<String> fieldPath = root.get(input.fieldName());
                return fieldPath.in((Object[]) values);
            };
            case STARTS_WITH -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(input.fieldName()),
                            input.filter().getValue().toString() + "%");
            case ENDS_WITH -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(input.fieldName()),
                            "%" + input.filter().getValue().toString());
            case LT -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get(input.fieldName()),
                            input.filter().getValue().toString());
            case GT -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get(input.fieldName()),
                            input.filter().getValue().toString());
            case DATE_BEFORE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get(input.fieldName()),
                            (LocalDateTime) input.filter().getValue());
            case DATE_AFTER -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get(input.fieldName()),
                            (LocalDateTime) input.filter().getValue());
            case DATE_IS -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(input.fieldName()),
                            input.filter().getValue());
            case DATE_IS_NOT -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.notEqual(root.get(input.fieldName()),
                            input.filter().getValue());
            default -> throw new GeneralException("Operation not supported yet");
        };
    }

    private Specification<Product> defaultSpec() {
        return (root, query, criteriaBuilder) -> {
            root.fetch(Product_.CATEGORY);
            root.fetch(Product_.PRODUCT_MEDIA_LIST);
            return criteriaBuilder.equal(root.get(Product_.IS_DELETE), false);
        };
    }
}
