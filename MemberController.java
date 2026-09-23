package com.example.msa_monolihic.member.controller;

import com.example.msa_monolihic.member.domain.Member;
import com.example.msa_monolihic.member.dto.LoginDTO;
import com.example.msa_monolihic.member.dto.MemberRefreshDTO;
import com.example.msa_monolihic.member.dto.MemberSaveReqDTO;
import com.example.msa_monolihic.member.service.JwtTokenProvider;
import com.example.msa_monolihic.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;  //주의
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    @Qualifier("rtdb")
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;


    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider, RedisTemplate<String, Object> redisTemplate) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    // 회원가입
    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberSaveReqDTO memberSaveReqDTO){
        System.out.println("<MemberController - memberCreate>");

        Long memberId = memberService.save(memberSaveReqDTO);
        return new ResponseEntity<>(memberId, HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginDTO dto){
        System.out.println("MemberController - doLogin");
        // email, password 검증
        Member member = memberService.login(dto);

        // 토큰 생성 및 return
        String token = jwtTokenProvider.createToken(member.getId().toString(), member.getRole().toString());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(),member.getRole().toString());

        // redis에 rt저장
        redisTemplate.opsForValue().set(member.getEmail(), refreshToken, 200, TimeUnit.DAYS);

        // 사용자에게 at, rt 지급
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", token);
        loginInfo.put("refreshToken", refreshToken);

        return new ResponseEntity<>(loginInfo,HttpStatus.OK);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> generateNewAt (@RequestBody MemberRefreshDTO dto){
        // rt 디코딩 후 email추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyRt)
                .build()
                .parseClaimsJws(dto.getRefreshToken())
                .getBody();

        // rt를 redis의 rt 비교 검증
        Object rt = redisTemplate.opsForValue().get(claims.getSubject());
        if (rt == null || !rt.toString().equals(dto.getRefreshToken())){    //저장된 redis의 토큰, 화면으로 넘어온 토큰 비교
            return new ResponseEntity<>((Object) null, HttpStatus.BAD_REQUEST); // 수정함
        }

        // at 생성하여 지급
        String token = jwtTokenProvider.createToken(claims.getSubject(), claims.get("role").toString());
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("token", token);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);

    }



}
