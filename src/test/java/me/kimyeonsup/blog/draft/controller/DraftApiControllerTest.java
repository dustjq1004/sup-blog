package me.kimyeonsup.blog.draft.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.kimyeonsup.blog.config.oauth.PrincipalDetail;
import me.kimyeonsup.blog.draft.domain.dto.AddDraftArticle;
import me.kimyeonsup.blog.draft.domain.dto.UpdateDraftArticle;
import me.kimyeonsup.blog.draft.domain.entity.DraftArticle;
import me.kimyeonsup.blog.draft.repository.DraftRepository;
import me.kimyeonsup.blog.login.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class DraftApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    DraftRepository draftRepository;

    PrincipalDetail principalDetail;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @BeforeEach
    void setSecurityContext() {
        User user = User.builder()
                .email("dustjq1005@gmail.com")
                .password("test")
                .role("관리자")
                .build();

        principalDetail = new PrincipalDetail(user);
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add((GrantedAuthority) user::getRole);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new OAuth2AuthenticationToken(principalDetail,
                authorities, "GM"));
    }

    @Test
    @DisplayName("임시 저장한 게시글을 조회한다.")
    void getDraftArticle() throws Exception {
        // given
        final String title = "title";
        final String subTitle = "subTitle";
        final String content = "content";
        final String author = "dustjq1005@gmail.com";

        List<AddDraftArticle> draftArticles = saveDraftArticles(title, subTitle, content, author);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/draft")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(draftArticles.size()))))
                .andExpect(content().string(containsString(title.concat(String.valueOf(draftArticles.size())))));

        log.info("mvcResult {}", resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    @DisplayName("게시글을 임시로 저장한다.")
    void saveDraftArticle() throws Exception {
        // given
        String title = "title";
        String subTitle = "subTitle";
        String content = "content";

        AddDraftArticle addDraftArticle = new AddDraftArticle(null, title, subTitle, content, 1L);

        String requestBody = objectMapper.writeValueAsString(addDraftArticle);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");
        // when
        ResultActions resultActions = mockMvc.perform(post("/api/draft")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(title)))
                .andExpect(content().string(containsString("dustjq1005@gmail.com")));
    }

    @ParameterizedTest
    @DisplayName("임시로 저장된 게시글을 수정한다.")
    @CsvSource({"1,수정1", "2,수정2", "3,수정3", "4,수정4"})
    void updateDraftArticle(Long id, String updateContent) throws Exception {
        // given
        final String title = "title";
        final String subTitle = "subTitle";
        final String content = "content";
        final String author = "dustjq1005@gmail.com";

        saveDraftArticles(title, subTitle, content, author);

        UpdateDraftArticle updatedArticle = new UpdateDraftArticle(id, title, subTitle, updateContent, 1L);
        String requestBody = objectMapper.writeValueAsString(updatedArticle);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/draft")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(updateContent)));
    }

    @ParameterizedTest
    @DisplayName("임시로 저장된 게시글 {id}을 삭제한다.")
    @CsvSource({"1", "2", "3", "4", "5"})
    void deleteTest(Long id) throws Exception {
        // given
        final String title = "title";
        final String subTitle = "subTitle";
        final String content = "content";
        final String author = "dustjq1005@gmail.com";

        saveDraftArticles(title, subTitle, content, author);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/draft/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal));

        List<DraftArticle> draftArticles = draftRepository.findAll()
                .stream()
                .filter((draftArticle) -> draftArticle.getId().equals(id))
                .toList();

        // then
        resultActions
                .andExpect(status().isOk());

        assertThat(draftArticles.isEmpty());
    }

    private List<AddDraftArticle> saveDraftArticles(String title, String subTitle, String content, String author) {
        List<AddDraftArticle> draftArticles = new ArrayList<>();
        final int length = 5;
        for (int i = 1; i <= length; i++) {
            AddDraftArticle addDraftArticle = new AddDraftArticle(null, title + i, subTitle + i, content + i, 1L);
            draftRepository.save(addDraftArticle.toEntity(author));
            draftArticles.add(addDraftArticle);
        }
        return draftArticles;
    }
}
