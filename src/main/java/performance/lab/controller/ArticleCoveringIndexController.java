package performance.lab.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import performance.lab.dto.ArticlePageResponse;
import performance.lab.dto.ArticleTtitleLikeCountResponse;
import performance.lab.service.ArticleCoveringIndexService;

@RestController
@RequiredArgsConstructor
public class ArticleCoveringIndexController {
    private final ArticleCoveringIndexService articleCoveringIndexService;

    @GetMapping("/api/article/title/coveringIndex")
    private ResponseEntity<List<ArticleTtitleLikeCountResponse>> findArticleTitleAndLikeCountWithCoveringIndex(
            @RequestParam("title") String title) {
        return ResponseEntity.ok(articleCoveringIndexService.findArticleByTitleAndLikeCount(title));
    }

    @GetMapping("/api/article/title/paging")
    public ResponseEntity<ArticlePageResponse> findArticleWithPaging(
            @RequestParam("title") String title,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleCoveringIndexService.findArticleWithPaging(title, page, size));
    }

    @GetMapping("/api/article/title/paging/covering")
    public ResponseEntity<ArticlePageResponse> findArticleWithPagingAndCoveringIndex(
            @RequestParam("title") String title,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(articleCoveringIndexService.findArticleWithPagingAndCoveringIndex(title, page, size));
    }
}
