package me.kimyeonsup.home.domain.blog.admin.article.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import me.kimyeonsup.home.domain.blog.admin.article.repository.AdminArticleRepository;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Category;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import me.kimyeonsup.home.domain.blog.admin.menu.repository.CategoryRepository;
import me.kimyeonsup.home.domain.blog.admin.menu.repository.MenuRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@SpringBootTest(properties = {
        "spring.sql.init.mode=never"
})
class AdminArticleServiceTest {

    @Autowired
    private AdminArticleService adminArticleService;

    @Autowired
    private AdminArticleRepository adminArticleRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void serviceBeanIsLoaded() {
        assertThat(adminArticleService).isNotNull();
    }

    @BeforeEach
    void setUp() {
        // 카테고리 생성
        Category category1 = Category.builder().name("category1").build();
        categoryRepository.saveAll(List.of(category1));

        // 메뉴 생성
        List<Menu> menus = List.of(
                Menu.builder().name("menu1").category(category1).build(),
                Menu.builder().name("menu2").category(category1).build(),
                Menu.builder().name("menu3").category(category1).build()
        );

        menuRepository.saveAll(menus);
        Menu menu = menus.get(0);

        // 테스트 데이터 생성
        createArticle("제목1", "sub title", "test content", "test author", menu);
        createArticle("제목2", "sub title", "test content", "test author", menu);
        createArticle("제목3", "test sub title", "test content", "test author", menu);
    }

    @AfterEach
    void tearDown() {
        // 데이터 베이스 초기화 - 다음 테스트 코드 영향
        adminArticleRepository.deleteAllInBatch();
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
    @DisplayName("블로그 글을 제목으로 조회한다. title에 포함된 글을 조회한다. 조회 결과는 2개이다.")
    void getArticlesByTitle() {
        // given
        List<Menu> menus = menuRepository.findAll();
        Menu menu = menus.get(0);
        String title = "test title";
        createArticle(title, "test sub title", "test content", "test author", menu);
        createArticle(title, "test sub title", "test content", "test author", menu);

        // when
        Page<AdminArticle> articles = adminArticleService.findArticlesBy(0, null, null);

        // then
        assertThat(articles.getTotalElements()).isEqualTo(2);
        articles.forEach(article -> {
            assertThat(article.getTitle()).contains(title);
        });
    }

    @Test
    @DisplayName("블로그 글을 작성 최신순으로 정렬한다.")
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
        Page<AdminArticle> articles = adminArticleService.findArticlesBy(pageNumber, null, null);

        // then
        assertThat(articles.getNumber()).isEqualTo(pageNumber);
        assertThat(articles.getNumberOfElements()).isEqualTo(pageSize);
    }

    private AdminArticle createArticle(String title, String subTitle, String content, String author, Menu menu) {
        AdminArticle created = AdminArticle.builder()
                .title(title)
                .subTitle(subTitle)
                .content(content)
                .author(author)
                .menu(menu)
                .build();
        adminArticleRepository.save(created);
        return created;
    }

}