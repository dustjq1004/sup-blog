package me.kimyeonsup.blog.article.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.kimyeonsup.blog.article.domain.dto.AddArticleRequest;
import me.kimyeonsup.blog.article.repository.ArticleRepository;
import me.kimyeonsup.blog.login.domain.entity.User;
import me.kimyeonsup.blog.login.repository.UserRepository;
import me.kimyeonsup.blog.menu.domain.entity.Menu;
import me.kimyeonsup.blog.menu.repository.MenuRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class AddArticleFailTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MenuRepository menuRepository;

    User user;
    Menu menu;

    private static final String url = "/api/articles";

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("dustjq1005@gmail.com")
                .password("test")
                .build());
        menu = menuRepository.save(Menu.builder()
                .name("자바")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user,
                user.getPassword(), user.getAuthorities()));
    }

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        articleRepository.deleteAll();
    }

    @DisplayName("validateTitle: 타이틀 유효성 검증을한다.")
    @ParameterizedTest
    @CsvSource(
            {
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, size must be between 0 and 50",
                    ",must not be blank"
            }
    )

    public void validateTitle(String title, String errorMessage) throws Exception {
        // given

        final String requestBody = getRequestBodyByTitle(title);
        log.info("requestBody : ${}", requestBody);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
        Assertions.assertThat(result.andReturn().getResolvedException().getMessage())
                .contains(errorMessage);
    }

    private String getRequestBodyByTitle(String title) throws JsonProcessingException {
        final String content = "";
        final String subTitle = "";

        final AddArticleRequest userRequest = new AddArticleRequest(title, subTitle, content, menu.getId());
        final String requestBody = objectMapper.writeValueAsString(userRequest);
        return requestBody;
    }
}
