package com.example.msa_monolihic.member.dto;


import com.example.msa_monolihic.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberSaveReqDTO {

    private String name;
    private String email;
    private String password;

    // 회원가입시점 - save() 호출 시
    public Member toEntity(String encodedPassword){
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .password(encodedPassword)
                .build();
    }


}
