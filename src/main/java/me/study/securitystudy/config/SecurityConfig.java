package me.study.securitystudy.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityExpressionHandler expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        return handler;
    }

    @Override
    public void configure(WebSecurity web) {
        // static 요청에 대해 검사하지 않음
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 특정 요청에 대해 설정
        // 인가
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**", "/signup").permitAll()  // 인증없이 접근 가능
                .mvcMatchers("/admin").hasRole("ADMIN")  // role 에 따라 구분
                .mvcMatchers("/user").hasRole("USER")  // role 에 따라 구분
                .anyRequest().authenticated()   // 기타 요청은 인증을 하기만 하면 됨
                .expressionHandler(expressionHandler());  // AccessDecisionManager 커스터마이징하여 설정

        // 인증
        http.formLogin();  // form login 을 사용
        http.httpBasic();  // http basic authentication 사용

        // 시큐리티 홀더의 공유 전략 설정 - 쓰레드가 생성하는 하위 쓰레드까지 자원공유
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}
