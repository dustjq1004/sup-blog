package me.kimyeonsup.blog.draft.repository;

import me.kimyeonsup.blog.draft.domain.entity.DraftArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DraftRepository extends JpaRepository<DraftArticle, Long> {

    List<DraftArticle> findByAuthorAndDelete(String author, Boolean delete);
}
