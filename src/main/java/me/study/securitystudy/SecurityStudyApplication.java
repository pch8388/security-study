package me.study.securitystudy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
public class SecurityStudyApplication implements CommandLineRunner {

    public SecurityStudyApplication(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(SecurityStudyApplication.class, args);
    }

    private final ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        final Object springSecurityFilterChain = applicationContext.getBean("springSecurityFilterChain");
        System.out.println("=======>>  " + springSecurityFilterChain.getClass());
    }
}
