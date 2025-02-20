package performance.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import performance.lab.dto.ArticleListResponse;
import performance.lab.service.ArticleCompositeIndexService;

@RestController
@RequiredArgsConstructor
public class ArticleCompositeIndexController {

    private final ArticleCompositeIndexService articleCompositeIndexService;

    @GetMapping("/api/article/title/likeCount")
    private ResponseEntity<ArticleListResponse> findArticleTitleAndLikeCount(
            @RequestParam("title") String title) {
        return ResponseEntity.ok(articleCompositeIndexService.findArticleByTitleAndLikeCount(title));
    }

    @GetMapping("/api/article/title/likeCount/compositeIndex")
    private ResponseEntity<ArticleListResponse> findArticletitleWithCompositeIndex(
            @RequestParam("title") String title) {
        return ResponseEntity.ok(articleCompositeIndexService.findArticleByTitleAndLikeCountWithCompositeIndex(title));
    }

    @GetMapping("/api/article/title/likeCount/reverse/compositeIndex")
    private ResponseEntity<ArticleListResponse> findArticletitleWithReverseCompositeIndex(
            @RequestParam("title") String title) {
        return ResponseEntity.ok(articleCompositeIndexService.findArticleWithReverseCompositeIndex(title));
    }
}
