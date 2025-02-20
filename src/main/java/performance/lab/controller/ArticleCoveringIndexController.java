package performance.lab.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
