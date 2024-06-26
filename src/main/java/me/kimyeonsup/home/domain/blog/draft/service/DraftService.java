package me.kimyeonsup.home.domain.blog.draft.service;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.draft.domain.dto.UpdateDraftArticle;
import me.kimyeonsup.home.domain.blog.draft.domain.dto.AddDraftArticle;
import me.kimyeonsup.home.domain.blog.draft.domain.entity.DraftArticle;
import me.kimyeonsup.home.domain.blog.draft.repository.DraftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DraftService {

    private final DraftRepository draftRepository;

    public List<DraftArticle> findByAuthorAndDelete(String author) {
        return draftRepository.findByAuthorAndDeleteYn(author, false);
    }

    public DraftArticle save(AddDraftArticle addDraftArticle, String author) {
        return draftRepository.save(addDraftArticle.toEntity(author));
    }

    @Transactional
    public DraftArticle update(UpdateDraftArticle draftArticle1) {
        DraftArticle draftArticle = draftRepository.findById(draftArticle1.getId())
                .orElseThrow(() -> new IllegalArgumentException("Long id is null or illegal"));

        return draftArticle.updateAll(draftArticle1);
    }

    public void delete(Long id) {
        DraftArticle draftArticle = draftRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("not found Draft" + id));
        draftRepository.delete(draftArticle);
    }
}
