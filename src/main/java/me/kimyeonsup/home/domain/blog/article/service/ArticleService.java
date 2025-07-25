package me.kimyeonsup.home.domain.blog.article.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kimyeonsup.home.config.oauth.PrincipalDetail;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import me.kimyeonsup.home.domain.blog.admin.menu.repository.MenuRepository;
import me.kimyeonsup.home.domain.blog.article.domain.dto.AddArticleRequest;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticlePrevNextDto;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticlePrevNextResponse;
import me.kimyeonsup.home.domain.blog.article.domain.dto.UpdateArticleRequest;
import me.kimyeonsup.home.domain.blog.article.domain.entity.Article;
import me.kimyeonsup.home.domain.blog.article.domain.vo.Thumbnail;
import me.kimyeonsup.home.domain.blog.article.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleService {

    private static final int PAGE_SIZE = 12;
    private static final String ORDER_CRITERIA = "createdAt";

    private final ArticleRepository articleRepository;
    private final MenuRepository menuRepository;

    public Article save(AddArticleRequest request, String userName) {
        Article savedArticle = articleRepository.save(request.toEntity(userName));
        return savedArticle;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Page<Article> findAllPagination(int pageNumber, Long categoryId, String menuName, String searchKeyword) {
        Pageable pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(Direction.DESC, ORDER_CRITERIA));

        Page<Article> articles = Optional.ofNullable(menuName)
                .map(name -> articleRepository.findByMenuNameAndTitleContaining(pageRequest, menuName, searchKeyword))
                .orElseGet(() -> Optional.ofNullable(categoryId)
                        .map(id -> articleRepository.findByCategoryIdAndTitleContaining(pageRequest, categoryId,
                                searchKeyword))
                        .orElseGet(() -> articleRepository.findByTitleContaining(searchKeyword, pageRequest)));

        return articles;
    }

    public List<Article> findByMenuId(Long menuId) {
        return articleRepository.findByMenuId(menuId);
    }

    public List<Article> findByMenuName(String menuName) {
        return articleRepository.findByMenuName(menuName);
    }

    public Article findById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        authorizeArticleAuthor(article);
        articleRepository.delete(article);
    }


    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("not found Menu: " + id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getSubTitle(), request.getContent(),
                Thumbnail.of(article.getContent()), menu);

        return article;
    }

    public ArticlePrevNextResponse findPrevNextArticle(long id, String menuName) {
        ArticlePrevNextDto prevNextArticle = articleRepository.findPrevNextArticle(id, menuName);
        return ArticlePrevNextResponse.builder()
                .articlePrevNextDto(prevNextArticle)
                .build();
    }

    private void authorizeArticleAuthor(Article article) {
        PrincipalDetail principalDetail = (PrincipalDetail) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if (!article.getAuthor().equals(principalDetail.getName())) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}
