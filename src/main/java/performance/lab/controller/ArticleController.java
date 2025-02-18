package performance.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import performance.lab.dto.ArticleListResponse;
import performance.lab.service.ArticleService;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService mainservice;

    @GetMapping("/api/article/{userId}")
    private ResponseEntity<ArticleListResponse> findArticle(@PathVariable("userId") Long userId) {
        ArticleListResponse response = mainservice.findArticle(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/article/search")
    private ResponseEntity<ArticleListResponse> findArticle(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(mainservice.findArticleByContent(keyword));
    }
}
