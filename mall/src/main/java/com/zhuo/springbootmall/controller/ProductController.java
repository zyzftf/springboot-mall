package com.zhuo.springbootmall.controller;

import com.zhuo.springbootmall.constant.ProductCategory;
import com.zhuo.springbootmall.dto.ProductQueryParams;
import com.zhuo.springbootmall.dto.ProductRequest;
import com.zhuo.springbootmall.model.Product;
import com.zhuo.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Valid
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            // 查詢條件 Filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,

            // 排序 Sorting
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,

            // 分頁 Pagination
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){

        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        List<Product> productList = productService.getProducts(productQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){

        Product product =  productService.getProductById(productId);

        if(product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        Integer productId = productService.createProduct(productRequest);
        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId, @RequestBody @Valid ProductRequest productRequest){

        // 檢查商品存不存在
        Product product = productService.getProductById(productId);

        if(product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 如果存在就更新商品
        productService.updateProduct(productId, productRequest);

        Product responseProduct = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(responseProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
