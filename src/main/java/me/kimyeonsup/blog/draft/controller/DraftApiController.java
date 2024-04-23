package me.kimyeonsup.blog.draft.controller;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.config.oauth.PrincipalDetail;
import me.kimyeonsup.blog.draft.domain.dto.AddDraftArticle;
import me.kimyeonsup.blog.draft.domain.dto.DraftArticlesResponse;
import me.kimyeonsup.blog.draft.domain.dto.UpdateDraftArticle;
import me.kimyeonsup.blog.draft.domain.entity.DraftArticle;
import me.kimyeonsup.blog.draft.service.DraftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DraftApiController {

    private final DraftService draftService;

    @GetMapping("/draft")
    public ResponseEntity<List<DraftArticlesResponse>> findByAuthorAnDelete(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<DraftArticlesResponse> draftArticles = draftService
                .findByAuthorAndDelete(principalDetail.getName())
                .stream()
                .map(DraftArticlesResponse::new)
                .toList();
        return ResponseEntity.ok(draftArticles);
    }

    @PostMapping("/draft")
    public ResponseEntity<DraftArticlesResponse> saveOrUpdate(@Validated @RequestBody AddDraftArticle addDraftArticle, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        DraftArticle savedDraftArticle = draftService.save(addDraftArticle, principalDetail.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new DraftArticlesResponse(savedDraftArticle));
    }

    @PutMapping("/draft")
    public ResponseEntity<DraftArticlesResponse> update(@Validated @RequestBody UpdateDraftArticle draftArticle, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        DraftArticle savedDraftArticle = draftService.update(draftArticle);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DraftArticlesResponse(savedDraftArticle));
    }

    @DeleteMapping("/draft/{id}")
    public ResponseEntity<Void> deleteDraftArticle(@PathVariable Long id) {
        draftService.delete(id);
        return ResponseEntity.ok()
                .build();
    }
}
