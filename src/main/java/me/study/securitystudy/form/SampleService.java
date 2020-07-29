package me.study.securitystudy.form;

import me.study.securitystudy.account.Account;
import me.study.securitystudy.account.AccountContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    public void dashboard() {
        final Account account = AccountContext.getAccount();
        System.out.println("account.getAccount() : " + account);

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("===============================");
        System.out.println(userDetails.getUsername());
    }
}
