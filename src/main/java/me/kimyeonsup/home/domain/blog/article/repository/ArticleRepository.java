package me.kimyeonsup.home.domain.blog.article.repository;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import me.kimyeonsup.home.domain.blog.article.domain.dto.ArticlePrevNextDto;
import me.kimyeonsup.home.domain.blog.article.domain.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByMenuId(Long menuId);

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.menu m WHERE m.name = :menuName")
    List<Article> findByMenuName(String menuName);

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.menu m WHERE m.name = :menuName")
    Page<Article> findByMenuName(Pageable pageRequest, String menuName);

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.menu m WHERE m.name = :menuName AND a.title LIKE %:searchKeyword%")
    Page<Article> findByMenuNameAndTitleContaining(Pageable pageRequest, String menuName, String searchKeyword);

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.menu m LEFT JOIN m.category c WHERE c.id = :categoryId")
    Page<Article> findByCategoryId(Pageable pageRequest, Long categoryId);

    @Query("SELECT a FROM Article a LEFT JOIN FETCH a.menu m LEFT JOIN m.category c WHERE c.id = :categoryId AND a.title LIKE %:searchKeyword%")
    Page<Article> findByCategoryIdAndTitleContaining(Pageable pageRequest, Long categoryId, String searchKeyword);

    /*
    SELECT
    	ID,
    	SUBJECT,
    	LAG(ID, 1, 0) OVER(ORDER BY ID ASC) AS PREBNO,
    	LAG(subject, 1, '이전글이 없습니다') OVER (ORDER BY ID) AS preb_sub,
    	LEAD(ID, 1, 0) OVER(ORDER BY ID ASC) AS NEXTBNO,
	LEAD(subject, 1, '다음글이 없습니다') OVER (ORDER BY ID) AS next_sub
FROM QUESTION
    * */
//    @Query(value = "SELECT b FROM (SELECT a.id, LAG(a.id, 1, 0) OVER(ORDER BY a.id ASC) AS prevId, "
//            + "LAG(a.title, 1, '이전글이 없습니다') OVER (ORDER BY a.id) AS prevTitle, "
//            + "LEAD(a.id, 1, 0) OVER(ORDER BY a.id ASC) AS nextId, "
//            + "LEAD(a.title, 1, '다음글이 없습니다') OVER (ORDER BY a.id) AS nextTitle "
//            + "FROM Article a LEFT JOIN a.menu m WHERE m.name = :menuName) b WHERE b.id = :id")
//    ArticlePrevNextDto findPrevNextArticle(Long id, String menuName);

    @Query(value =
            "SELECT b.id AS id, b.prevId AS prevId, b.prevTitle AS prevTitle, b.nextId AS nextId, b.nextTitle AS nextTitle FROM ("
                    + "SELECT a.id AS id, "
                    + "LAG(a.id, 1, 0) OVER(ORDER BY a.id ASC) AS prevId, "
                    + "LAG(a.title, 1, '이전글이 없습니다') OVER (ORDER BY a.id) AS prevTitle, "
                    + "LEAD(a.id, 1, 0) OVER(ORDER BY a.id ASC) AS nextId, "
                    + "LEAD(a.title, 1, '다음글이 없습니다') OVER (ORDER BY a.id) AS nextTitle "
                    + "FROM Article a LEFT JOIN a.menu m WHERE m.name = :menuName) b WHERE b.id = :id")
    ArticlePrevNextDto findPrevNextArticle(Long id, String menuName);


    List<Article> findTop8ByOrderByCreatedAtDesc();

    List<Article> findByTitleContains(@NotBlank String titleParam);

    Page<Article> findByTitleContaining(String title, Pageable pageable);
}
