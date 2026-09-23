package com.example.msa_monolihic.ordering.service;


import com.example.msa_monolihic.member.domain.Member;
import com.example.msa_monolihic.member.repository.MemberRepository;
import com.example.msa_monolihic.ordering.domain.Ordering;
import com.example.msa_monolihic.ordering.dto.OrderCreateDTO;
import com.example.msa_monolihic.ordering.repository.OrderingRepository;
import com.example.msa_monolihic.product.domain.Product;
import com.example.msa_monolihic.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderingService {

    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public OrderingService(OrderingRepository orderingRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.orderingRepository = orderingRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    //제품주문
    public Ordering orderCreate(OrderCreateDTO orderdto){

        System.out.println("OrderingService - orderCreate");

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findById(Long.parseLong(id)).orElseThrow(()->new EntityNotFoundException("member is not found"));

        Product product = productRepository.findById(orderdto.getProductId()).orElseThrow(()-> new EntityNotFoundException("product is not found"));
        int quantity = orderdto.getProductCount();
        if(product.getStockQuantity() < quantity) { //재고수량 < 주문수량
            throw new IllegalArgumentException("재고 부족");
        }else {
            product.updateStockQuantity(orderdto.getProductCount());
        }
        Ordering ordering = Ordering.builder()
                .member(member)
                .product(product)
                .quantity(orderdto.getProductCount())
                .build();
        orderingRepository.save(ordering);
        return ordering;
    }
}
