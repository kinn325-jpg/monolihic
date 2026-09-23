package com.example.msa_monolihic.product.service;

import com.example.msa_monolihic.member.domain.Member;
import com.example.msa_monolihic.member.repository.MemberRepository;
import com.example.msa_monolihic.product.domain.Product;
import com.example.msa_monolihic.product.dto.ProductRegisterDTO;
import com.example.msa_monolihic.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ProductService(ProductRepository productRepository, MemberRepository memberRepository, ProductRepository productRepository1, MemberRepository memberRepository1){

        this.productRepository = productRepository1;
        this.memberRepository = memberRepository1;
    }

    // 제품등록
    public Product productCreate(ProductRegisterDTO dto){
        System.out.println("ProductService - productCreate");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(()->new EntityNotFoundException("member is not found"));

        Product product = productRepository.save(dto.toEntity(member));
        return product;
    }

    // 제품 상세
    public  Product productDetail(Long id){
        System.out.println("<<< ProductService - productDetail>>>");

         return productRepository.findById(id).get();
    }

    //제품 수정
    public Product productUpdate(Product dto){
        System.out.println("<<<ProductService - productUpdate>>>");

        return productRepository.save(dto);
    }



}
