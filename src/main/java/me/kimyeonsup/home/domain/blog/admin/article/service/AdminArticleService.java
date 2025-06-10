package me.kimyeonsup.home.domain.blog.admin.article.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.article.domain.dto.ArticleSelectCondition;
import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import me.kimyeonsup.home.domain.blog.admin.article.repository.AdminArticleRepository;
import me.kimyeonsup.home.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminArticleService {

    private final AdminArticleRepository adminArticleRepository;

    /*
     * 필터 - 카테고리, 메뉴
     * 정렬 - 제목, 시간 순으로 정렬 가능
     * Paging 처리
     * -> PageRequest 빌더패턴 변경 예정
     * */
    public Page<AdminArticle> findArticlesBy(PageRequest pageRequest, ArticleSelectCondition params) {
        final Long categoryId = params.getCategoryId();
        final Long menuId = params.getMenuId();
        final String title = StringUtils.nvl(params.getTitle(), "");

        // 필터 구현 - 메뉴
        if (Objects.nonNull(menuId)) {
            return adminArticleRepository.findByMenuIdAndTitleContaining(pageRequest, menuId, title);
        }
        // 필터 구현 - 카테고리
        if (Objects.nonNull(categoryId)) {
            return adminArticleRepository.findByMenu_CategoryIdAndTitleContaining(pageRequest, categoryId, title);
        }

        return adminArticleRepository.findByTitleContaining(pageRequest, title);
    }

}
