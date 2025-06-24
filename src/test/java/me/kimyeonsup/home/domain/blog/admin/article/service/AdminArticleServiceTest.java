package me.kimyeonsup.home.domain.blog.admin.article.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import me.kimyeonsup.home.domain.blog.admin.article.domain.dto.ArticleSelectCondition;
import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import me.kimyeonsup.home.domain.blog.admin.article.repository.AdminArticleRepository;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Category;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import me.kimyeonsup.home.domain.blog.admin.menu.repository.CategoryRepository;
import me.kimyeonsup.home.domain.blog.admin.menu.repository.MenuRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

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

    @BeforeAll
    static void beforeAll() {
        // 테스트 데이터베이스 초기화
        // 이 메서드는 모든 테스트가 실행되기 전에 한 번만 실행됩니다.
        // 예를 들어, 테스트 데이터베이스를 초기화하는 작업을 수행할 수 있습니다.

    }

    @BeforeEach
    void setUp() {
        // 카테고리 생성
        Category category1 = Category.builder().name("category1").build();
        Category category2 = Category.builder().name("category2").build();
        Category category3 = Category.builder().name("category3").build();
        categoryRepository.saveAll(List.of(category1, category2, category3));

        // 메뉴 생성
        List<Menu> menus = List.of(
                Menu.builder().name("menu1").category(category1).build(),
                Menu.builder().name("menu2").category(category2).build(),
                Menu.builder().name("menu3").category(category3).build()
        );

        menuRepository.saveAll(menus);
        Menu menu = menus.get(0);

        // 테스트 데이터 생성
        createArticle("제목1", "sub title", "test content", "test author", menu);
        createArticle("제목2", "sub title", "test content", "test author", menu);
        createArticle("제목3", "test sub title", "test content", "test author", menu);
        for (int i = 0; i < 30; i++) {
            createArticle("제목" + i, "test sub title", "test content", "test author",
                    menus.get(new Random().nextInt(menus.size())));
        }
    }

    @AfterEach
    void tearDown() {
        // 데이터 베이스 초기화 - 다음 테스트 코드 영향
        // adminArticleRepository.deleteAllInBatch();
        // menuRepository.deleteAllInBatch();
        // categoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("블로그 글을 조회한다.")
    void getArticles() {
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(Direction.DESC, "createdAt"));
        Page<AdminArticle> articles = adminArticleService.findArticlesBy(pageRequest,
                ArticleSelectCondition.builder().build());

        assertThat(articles.getTotalElements()).isNotZero();
    }

    @ParameterizedTest
    @DisplayName("블로그 글을 카테고리로 조회한다.")
    @ValueSource(longs = {1L, 2L, 3L})
    void getArticlesByCategory(Long categoryId) {
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Direction.DESC, "createdAt"));
        ArticleSelectCondition params = ArticleSelectCondition.builder()
                .categoryId(categoryId)
                .build();

        Page<AdminArticle> articles = adminArticleService.findArticlesBy(pageRequest, params);

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
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(Direction.DESC, "createdAt"));
        // when
        ArticleSelectCondition params = ArticleSelectCondition.builder()
                .menuId(menuId)
                .build();

        Page<AdminArticle> articles = adminArticleService.findArticlesBy(pageRequest, params);

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
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(Direction.DESC, "createdAt"));
        ArticleSelectCondition params = ArticleSelectCondition.builder()
                .title(title)
                .build();

        // when

        Page<AdminArticle> articles = adminArticleService.findArticlesBy(pageRequest,
                params);

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
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by(Direction.DESC, "createdAt"));

        // when
        var articles = adminArticleService.findArticlesBy(pageRequest, ArticleSelectCondition.builder().build());

        // then
        assertThat(articles.getPageable().getSort()).isEqualTo(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Test
    @DisplayName("블로그 글을 제목 순으로 정렬한다.")
    void getArticlesOrderByTitle() {
        // given
        int pageNumber = 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, 20, Sort.by(Direction.ASC, "title"));
        // when
        var articles = adminArticleService.findArticlesBy(pageRequest, ArticleSelectCondition.builder().build());

        // then
        assertThat(articles.getPageable().getSort()).isEqualTo(Sort.by(Sort.Direction.ASC, "title"));
    }

    @Test
    @DisplayName("블로그 글을 20개씩 1페이지를 조회한다.")
    void getArticlesByPage() {
        // given
        int pageNumber = 0;
        int pageSize = 20;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.DESC, "createdAt"));
        // when
        Page<AdminArticle> articles = adminArticleService.findArticlesBy(pageRequest,
                ArticleSelectCondition.builder().build());

        // then
        assertThat(articles.getNumber()).isEqualTo(pageNumber);
        assertThat(articles.getNumberOfElements()).isEqualTo(pageSize);
    }

    @Test
    @DisplayName("블로그 글을 3개 일괄 삭제한다.")
    void deleteArticlesByIds() {
        // given
        List<AdminArticle> articles = adminArticleRepository.findAll();
        Long[] articleIds = articles.stream().map(AdminArticle::getId).limit(3).toArray(Long[]::new);

        // when
        int deletedCount = adminArticleService.deleteArticlesByIds(List.of(articleIds));

        // then
        assertThat(deletedCount).isEqualTo(3);
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