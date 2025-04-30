package me.kimyeonsup.home.domain.blog.admin.article.service;

import static org.assertj.core.api.Assertions.assertThat;

import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import me.kimyeonsup.home.domain.blog.admin.article.repository.AdminArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@SpringBootTest
class AdminArticleServiceTest {

    @Autowired
    private AdminArticleService adminArticleService;

    @Autowired
    private AdminArticleRepository adminArticleRepository;

    @Test
    void serviceBeanIsLoaded() {
        assertThat(adminArticleService).isNotNull();
    }

    @Test
    @DisplayName("블로그 글을 조회한다.")
    void getArticles() {
        Page<AdminArticle> articles = adminArticleService.findArticlesBy(0, null, null);

        assertThat(articles.getTotalElements()).isNotZero();
    }

    @ParameterizedTest
    @DisplayName("블로그 글을 카테고리로 조회한다.")
    @ValueSource(longs = {1L, 2L, 3L})
    void getArticlesByCategory(Long categoryId) {
        Page<AdminArticle> articles = adminArticleService.findArticlesBy(0, categoryId, null);

        assertThat(articles.getTotalElements()).isNotZero();
        articles.forEach(article -> {
            assertThat(article.getMenu().getCategory().getId()).isEqualTo(categoryId);
        });
    }

    @Test
    @DisplayName("블로그 글을 메뉴로 조회한다.")
    void getArticlesByMenu() {
        // given
        Long menuId = 1L;

        // when
        Page<AdminArticle> articles = adminArticleService.findArticlesBy(0, null, menuId);

        // then
        assertThat(articles.getTotalElements()).isNotZero();
        articles.forEach(article -> {
            assertThat(article.getMenu().getId()).isEqualTo(menuId);
        });
    }

    @Test
    @DisplayName("블로그 글을 제목으로 조회한다.")
    void getArticlesByTitle() {
        // given
        String title = "test";
        AdminArticle created = AdminArticle.builder()
                .title(title)
                .subTitle("test sub title")
                .content("test content")
                .author("test author")
                .build();

        adminArticleRepository.save(created);

        // when
        Page<AdminArticle> articles = adminArticleService.findArticlesBy(0, null, null);

        // then
        assertThat(articles.getTotalElements()).isNotZero();
        articles.forEach(article -> {
            assertThat(article.getTitle()).contains(title);
        });
    }

    @Test
    @DisplayName("블로그 글을 작성순으로 정렬한다.")
    void getArticlesOrderByCreatedAt() {
        // given
        int pageNumber = 0;
        int pageSize = 20;

        // when
        var articles = adminArticleService.findArticlesBy(pageNumber, null, null);

        // then
        assertThat(articles.getPageable().getSort()).isEqualTo(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Test
    @DisplayName("블로그 글을 제목 순으로 정렬한다.")
    void getArticlesOrderByTitle() {
        // given
        int pageNumber = 0;
        int pageSize = 20;

        // when
        var articles = adminArticleService.findArticlesBy(pageNumber, null, null);

        // then
        assertThat(articles.getPageable().getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, "title"));
    }

    @Test
    @DisplayName("블로그 글을 20개씩 1페이지를 조회한다.")
    void getArticlesByPage() {
        // given
        int pageNumber = 0;
        int pageSize = 20;

        // when
        var articles = adminArticleService.findArticlesBy(pageNumber, null, null);

        // then
        //assertThat(articles.getPageNumber()).isEqualTo(pageNumber);
        //assertThat(articles.getPageSize()).isEqualTo(pageSize);
    }

}