package performance.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import performance.lab.dto.ArticleListResponse;
import performance.lab.service.ArticleIndexService;

@RestController
@RequiredArgsConstructor
public class ArticleIndexController {

    private final ArticleIndexService articleIndexService;

    @GetMapping("/api/article/{userId}")
    private ResponseEntity<ArticleListResponse> findArticle(@PathVariable("userId") Long userId) {
        ArticleListResponse response = articleIndexService.findArticle(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/article/search")
    private ResponseEntity<ArticleListResponse> findArticle(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(articleIndexService.findArticleByContent(keyword));
    }

    @GetMapping("/api/article/indexSearch")
    private ResponseEntity<ArticleListResponse> findArticleWithIndex(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(articleIndexService.findArticleByContentWithIndexing(keyword));
    }

    @GetMapping("/api/article/coveringIndexSearch")
    private ResponseEntity<ArticleListResponse> findArticleWithCoveringIndex(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(articleIndexService.findArticleByContentWithCoveringIndexing(keyword));
    }

    @GetMapping("/api/article/title/indexSearch")
    private ResponseEntity<ArticleListResponse> findArticleTitleWithIndex(@RequestParam("title") String title) {
        return ResponseEntity.ok(articleIndexService.findArticleByTitleWithIndexing(title));
    }

    @GetMapping("/api/article/title/coveringIndexSearch")
    private ResponseEntity<ArticleListResponse> findArticleTitleWithCoveringIndex(
            @RequestParam("title") String title) {
        return ResponseEntity.ok(articleIndexService.findArticleByTitleWithCoveringIndexing(title));
    }
}
