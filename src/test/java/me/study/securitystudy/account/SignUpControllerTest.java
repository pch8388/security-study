package me.study.securitystudy.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class SignUpControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void signUpForm() throws Exception {
        mockMvc.perform(get("/signup"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("_csrf")));
    }

    @Test
    public void processSignUp() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("username", "sc")
                        .param("password", "123")
                        .with(csrf()))   // 적절한 csrf 토큰을 넣어준다
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}