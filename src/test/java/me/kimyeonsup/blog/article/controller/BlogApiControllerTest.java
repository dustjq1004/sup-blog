package me.kimyeonsup.blog.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kimyeonsup.blog.article.domain.dto.AddArticleRequest;
import me.kimyeonsup.blog.article.domain.dto.ArticlePrevNextDto;
import me.kimyeonsup.blog.article.domain.dto.UpdateArticleRequest;
import me.kimyeonsup.blog.article.domain.entity.Article;
import me.kimyeonsup.blog.article.repository.ArticleRepository;
import me.kimyeonsup.blog.login.domain.entity.User;
import me.kimyeonsup.blog.login.repository.UserRepository;
import me.kimyeonsup.blog.menu.domain.entity.Menu;
import me.kimyeonsup.blog.menu.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

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

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        List<Menu> menus = menuRepository.findAll();
        Menu findMenu = menus.get(0);
        final AddArticleRequest userRequest = new AddArticleRequest(title, "", content, findMenu.getId());

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = articleRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles : 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticle() throws Exception {
        // given
        final String url = "/blog";
        Article savedArticle = createDefaultArticle();

        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.TEXT_HTML));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(savedArticle.getContent())))
                .andExpect(content().string(containsString(savedArticle.getTitle())));

    }

    @DisplayName("findAllArticles : 블로그 페이지네이션 조회에 성공한다.")
    @Test
    public void findAllArticlePagination() throws Exception {
        // given
        final String url = "/api/articles?pageNumber=0";
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            articles.add(createDefaultArticle());
        }

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpectAll(jsonPath("$.totalCount").value(articles.size()));

    }

    @DisplayName("findArtcle : 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        // when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("<p>" + savedArticle.getContent() + "</p>\n"))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        // when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // then
        List<Article> articles = articleRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        final String newTitle = "new title";
        final String newSubTitle = "sub Title";
        final String newContent = "new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newSubTitle, newContent, menu.getId());

        // when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk());

        Article article = articleRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    @DisplayName("이전글 다음글을 조회한다.")
    @Test
    void getPrevNextArticle() {

        String menuName = "자바";
        createDefaultArticle();
        createDefaultArticle();
        createDefaultArticle();

        List<Article> all = articleRepository.findAll();
        Long id = all.get(1).getId();

        ArticlePrevNextDto prevNextArticle = articleRepository.findPrevNextArticle(id, menuName);

        assertThat(all.size()).isEqualTo(3);

        assertThat(prevNextArticle.getId()).isEqualTo(id);
        assertThat(prevNextArticle.getNextId()).isEqualTo(id + 1);
        assertThat(prevNextArticle.getPrevTitle()).isEqualTo("title");
        assertThat(prevNextArticle.getPrevId()).isEqualTo(14);
        assertThat(prevNextArticle.getNextTitle()).isEqualTo("title");
    }

    private Article createDefaultArticle() {
        return articleRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .menu(menu)
                .build());
    }
}
