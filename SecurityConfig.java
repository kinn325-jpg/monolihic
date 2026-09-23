package com.example.msa_monolihic.common.config;

import com.example.msa_monolihic.common.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter authFilter;

    public SecurityConfig(JwtAuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity
                .cors(c -> c.configurationSource(corsConfiguration()))
                .csrf(AbstractHttpConfigurer::disable)  //csrf (보안공격중 하나 ) 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) //http basic 보안방식 비활성화
                // 세션로그인 방식 사용하지 않겠다는 것을 의미(토큰사용)
                .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // token을 검증하고, token을 통해 Authentication 객체생성
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(a -> a.requestMatchers("/member/create", "/member/doLogin",
                        "/member/refresh-token","/product/list")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .build();
    }

    private CorsConfigurationSource corsConfiguration(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));    //프론트엔드
        configuration.setAllowedOrigins(Arrays.asList("*"));    // 모든 HTTP 메서드(get, post 등) 허용
        configuration.setAllowedOrigins(Arrays.asList("*"));    // 모든 헤더 허용
        configuration.setAllowCredentials(true);    // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder makePassword(){

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
