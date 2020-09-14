package me.study.securitystudy.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        http.logout()
            .logoutUrl("/logout")  // logout url 설정 => 커스텀한 페이지를 설정하고 싶을 때
            .logoutSuccessUrl("/");  // logout 성공시 이동할 url

        // 커스텀 로그인 페이지
        http.formLogin()
            .loginPage("/login")   // get 요청은 로그인 폼 페이지를 보여주고, post 요청은 UsernamePasswordAuthenticationFilter 에서 검증
            .permitAll();

        http.sessionManagement()
            .maximumSessions(1)  // 같은 계정으로 동시 로그인 최대 개수
                .maxSessionsPreventsLogin(false); // 동시 접속이 생기면 기존의 세션을 false : 만료시킴, true : 새로운 접속 불가

        http.exceptionHandling()
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username = principal.getUsername();
                System.out.println(username + " is denied to access " + request.getRequestURI());
                response.sendRedirect("/access-denied");
            }); // 핸들러를 구현하여 인가 실패에 대한 처리

        // 시큐리티 홀더의 공유 전략 설정 - 쓰레드가 생성하는 하위 쓰레드까지 자원공유
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        SecurityContextHolder.setStrategyName("me.study.securitystudy.common.CustomSecurityContextHolderStrategy");
    }
}
