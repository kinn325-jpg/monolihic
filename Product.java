package com.example.msa_monolihic.product.domain;

import com.example.msa_monolihic.common.domain.BaseTimeEntity;
import com.example.msa_monolihic.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // product id

    private String name;    //product명
    private Integer price;  //가격
    private Integer stockQuantity; //수량

    @ManyToOne(fetch = FetchType.LAZY)  //JPA에서 다대일(N대1) 관계를 매핑할 때 사용하는 이노테이션
    @JoinColumn(name = "member_id")
    private Member member;  //회원정보

    public void updateStockQuantity(int stockQuantity){
        this.stockQuantity = this.stockQuantity - stockQuantity;
    }


}
