package com.example.msa_monolihic.product.controller;


import com.example.msa_monolihic.product.domain.Product;
import com.example.msa_monolihic.product.dto.ProductRegisterDTO;
import com.example.msa_monolihic.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 제품 추가
    @PostMapping("/create")
    public ResponseEntity<?> productCreate(ProductRegisterDTO dto){
        System.out.println("ProductController - productCreate");

        Product product = productService.productCreate(dto);
        return new ResponseEntity<>(product.getId(), HttpStatus.CREATED);
    }

    // 제품 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> productDetail(@PathVariable Long id) {
        System.out.println("<<< ProductController - productDetail>>>");

        return new ResponseEntity<>(productService.productDetail(id),HttpStatus.OK);
    }

    // 제품 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<?> productUpdate(@RequestBody Product dto){
        System.out.println("<<< ProductController - productUpdate>>>");

        Product product = productService.productUpdate(dto);
        return new ResponseEntity<>(product.getId(), HttpStatus.OK);
    }



}
