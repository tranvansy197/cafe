package com.cafe.server.controller.endpoint;

import com.cafe.model.core.PageResponseDTO;
import com.cafe.model.dto.product.ProductDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface ProductEndPoint {
    @GetMapping("GetAllProducts")
    @ResponseStatus(value = HttpStatus.OK)
    PageResponseDTO<ProductDTO> getAllProducts(String strPagination, String strFilter, String strSort);

    @GetMapping("GetProduct/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    ProductDTO getProductById(@PathVariable Long id);

    @PostMapping("addProduct")
    @ResponseStatus(value = HttpStatus.CREATED)
    void addProduct(@RequestParam String strProduct,@RequestParam MultipartFile[] files);

    @PutMapping("updateProduct/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    void updateProduct(@PathVariable Long id, @RequestParam String strProduct, MultipartFile[] file);

}
