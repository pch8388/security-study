package me.study.securitystudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 특정 요청에 대해 설정
        // 인가
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**").permitAll()  // 인증없이 접근 가능
                .mvcMatchers("/admin").hasRole("ADMIN")  // role 에 따라 구분
                .anyRequest().authenticated();   // 기타 요청은 인증을 하기만 하면 됨

        // 인증
        http.formLogin();  // form login 을 사용
        http.httpBasic();  // http basic authentication 사용
    }
}
