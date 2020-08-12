package me.study.securitystudy.form;

import lombok.RequiredArgsConstructor;
import me.study.securitystudy.account.Account;
import me.study.securitystudy.account.AccountContext;
import me.study.securitystudy.account.AccountRepository;
import me.study.securitystudy.account.UserAccount;
import me.study.securitystudy.book.BookRepository;
import me.study.securitystudy.common.CurrentUser;
import me.study.securitystudy.common.SecurityLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Optional;
import java.util.concurrent.Callable;

@Controller
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;
    private final AccountRepository accountRepository;
    private final BookRepository bookRepository;
    private final ApplicationContext context;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserAccount userAccount) {
        Optional.ofNullable(userAccount)
            .ifPresentOrElse(p -> model.addAttribute("message", "Hello, " + p.getUsername()),
                () -> model.addAttribute("message", "Hello Spring Security"));

        return "index";
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("message", "info");
        return "info";
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model, @CurrentUser Account account) {
        model.addAttribute("message", "Hello " + account.getUsername());
        AccountContext.setAccount(accountRepository.findByUsername(account.getUsername()));
        sampleService.dashboard();
        return "dashboard";
    }


    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("message", "Hello Admin, " + principal.getName());
        return "admin";
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal) {
        model.addAttribute("message", "Hello User, " + principal.getName());
        model.addAttribute("books", bookRepository.findCurrentUserBooks());
        return "user";
    }

    @GetMapping("/async-handler")
    @ResponseBody
    public Callable<String> asyncHandler() {
        SecurityLogger.log("MVC");

        return () -> {
            SecurityLogger.log("Callable");
            return "Async Handler";
        };
    }

    @GetMapping("/async-service")
    @ResponseBody
    public String asyncService() {
        SecurityLogger.log("MVC, before async service");
        sampleService.asyncService();
        SecurityLogger.log("MVC, after async service");
        return "Async Service";
    }
}
