package com.example.msa_monolihic.product.dto;


import com.example.msa_monolihic.product.domain.Product;
import com.example.msa_monolihic.member.domain.Member;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="monolihic_board_tbl")
public class ProductRegisterDTO {

    //@SequenceGenerator(schema="")


    private String name;    //제품명
    private String category; //카테고리
    private int price;  //제품가격
    private int stockQuantity;  //제품수량

    public Product toEntity(Member member){
        return Product.builder()
                .name(this.name)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .member(member)
                .build();
    }
}
