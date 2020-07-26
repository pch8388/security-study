package me.study.securitystudy.form;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;

@Controller
public class SampleController {

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        Optional.ofNullable(principal)
            .ifPresentOrElse(p -> model.addAttribute("message", "Hello, " + p.getName()),
                () -> model.addAttribute("message", "Hello Spring Security"));

        return "index";
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("message", "info");
        return "info";
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("message", "Hello " + principal.getName());
        return "dashboard";
    }


    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("message", "Hello Admin, " + principal.getName());
        return "admin";
    }
}
