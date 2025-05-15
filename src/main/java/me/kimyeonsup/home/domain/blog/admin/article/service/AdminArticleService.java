package me.kimyeonsup.home.domain.blog.admin.article.service;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import me.kimyeonsup.home.domain.blog.admin.article.repository.AdminArticleRepository;
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
    public Page<AdminArticle> findArticlesBy(PageRequest pageRequest, final Long categoryId, final Long menuId) {
        // 필터 구현 - 메뉴
        if (menuId != null) {
            return adminArticleRepository.findByMenuId(pageRequest, menuId);
        }
        // 필터 구현 - 카테고리
        if (categoryId != null) {
            return adminArticleRepository.findByMenu_CategoryId(categoryId);
        }

        return adminArticleRepository.findAll(pageRequest);
    }

}
