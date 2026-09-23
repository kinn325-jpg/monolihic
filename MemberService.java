package com.example.msa_monolihic.member.service;


import com.example.msa_monolihic.member.domain.Member;
import com.example.msa_monolihic.member.dto.MemberSaveReqDTO;
import com.example.msa_monolihic.member.dto.LoginDTO;
import com.example.msa_monolihic.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    // 멤버변수
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // setter 설정
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    public Long save(MemberSaveReqDTO memberSaveReqDTO){

        System.out.println("<MemberService - save()>");
        Optional<Member> optionalMember = memberRepository.findByEmail(memberSaveReqDTO.getEmail());

        if(optionalMember.isPresent()){
            throw new IllegalArgumentException("기존에 존재하는 회원입니다.");
        }
        // encode(암호화전 비밀번호)
        String password = passwordEncoder.encode(memberSaveReqDTO.getPassword());
        Member member = memberRepository.save(memberSaveReqDTO.toEntity(password));
        System.out.println("password : " + password);
        return member.getId();
    }

    // 로그인
    public Member login(LoginDTO dto){

        System.out.println("<MemberService - login>");

        boolean check = true;

        // email 존재여부
        Optional<Member> optionalMember = memberRepository.findByEmail(dto.getEmail());
        if(!optionalMember.isPresent()){
            check = false;
        }

        // password 일치 여부
        if(!passwordEncoder.matches(dto.getPassword(), optionalMember.get().getPassword())){
            check = false;
        }
        if(!check){
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return optionalMember.get();
    }


}
