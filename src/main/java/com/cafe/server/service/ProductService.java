package com.cafe.server.service;

import com.cafe.model.core.PageResponseDTO;
import com.cafe.model.dto.product.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    PageResponseDTO<ProductDTO> getAllProducts(String strPagination, String strFilter, String strSort);

    ProductDTO getProductById(Long id);

    void createProduct(String strProduct, MultipartFile[] file);
    void updateProduct(Long id, String productDTO, MultipartFile[] file);

    void deleteProduct(Long id);
}
