package me.study.securitystudy.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    public void index_anonymous() throws Exception {
        mockMvc.perform(get("/").with(anonymous()))  // 익명으로 접근 시
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void index_anonymous_annotation() throws Exception {
        mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void index_user() throws Exception {
        // with (sc 이름의 USER 롤을 가진 유저가 로그인한 상태에서 url 요청하였을때) << mocking
        mockMvc.perform(get("/").with(user("sc").roles("USER")))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithUser       // 커스텀한 어노테이션을 붙여서 간략화 할 수 있다
    public void admin_user() throws Exception {
        mockMvc.perform(get("/admin"))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "sc", roles = "ADMIN")
    public void admin_admin() throws Exception {
        mockMvc.perform(get("/admin"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void login_success() throws Exception {
        final String username = "sc";
        final String password = "123";
        final Account user = createUser(username, password);
        // form login 검증
        mockMvc.perform(formLogin().user(user.getUsername()).password(password))
                .andExpect(authenticated());  // 인증이 되는지 확인
    }

    @Test
    @Transactional
    public void login_fail() throws Exception {
        final String username = "sc";
        final String password = "123";
        final Account user = createUser(username, password);
        // form login 검증
        mockMvc.perform(formLogin().user(user.getUsername()).password("12345"))
            .andExpect(unauthenticated());  // 인증이 되는지 확인 -> 인증되지 않아야 통과
    }

    private Account createUser(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole("USER");
        return accountService.createNew(account);
    }
}