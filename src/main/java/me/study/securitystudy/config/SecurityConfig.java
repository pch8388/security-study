package me.study.securitystudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public AccessDecisionManager accessDecisionManager() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(handler);
        List<AccessDecisionVoter<? extends Object>> voters = Arrays.asList(webExpressionVoter);
        return new AffirmativeBased(voters);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 특정 요청에 대해 설정
        // 인가
        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**").permitAll()  // 인증없이 접근 가능
                .mvcMatchers("/admin").hasRole("ADMIN")  // role 에 따라 구분
                .mvcMatchers("/user").hasRole("USER")  // role 에 따라 구분
                .anyRequest().authenticated()   // 기타 요청은 인증을 하기만 하면 됨
                .accessDecisionManager(accessDecisionManager());  // AccessDecisionManager 커스터마이징하여 설정

        // 인증
        http.formLogin();  // form login 을 사용
        http.httpBasic();  // http basic authentication 사용
    }
}
