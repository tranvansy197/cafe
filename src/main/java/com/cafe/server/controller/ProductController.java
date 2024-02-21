package com.cafe.server.controller;

import com.cafe.model.constant.APIConstant;
import com.cafe.model.core.PageResponseDTO;
import com.cafe.model.dto.product.ProductDTO;
import com.cafe.server.controller.endpoint.ProductEndPoint;
import com.cafe.server.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(APIConstant.API_URL_PRODUCT)
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductEndPoint {
    private final ProductService productService;

    @Override
    public PageResponseDTO<ProductDTO> getAllProducts(String strPagination, String strFilter, String strSort) {
        return productService.getAllProducts(strPagination, strFilter, strSort);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productService.getProductById(id);
    }

    @Override
    public void addProduct(String strProduct, MultipartFile[] file) {
        productService.createProduct(strProduct, file);
    }

    @Override
    public void updateProduct(Long id, String strProduct, MultipartFile[] file) {
        productService.updateProduct(id, strProduct, file);
    }
}
