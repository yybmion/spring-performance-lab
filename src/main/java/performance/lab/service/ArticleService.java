package performance.lab.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import performance.lab.domain.Article;
import performance.lab.dto.ArticleListResponse;
import performance.lab.monitor.LogExecutionTime;
import performance.lab.repository.ArticleRepository;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleListResponse findArticle(Long userId) {
        List<Article> articleList = articleRepository.findByUserId(userId);

        return ArticleListResponse.createResponse(articleList);
    }

    @LogExecutionTime
    public ArticleListResponse findArticleByContent(String keyword) {
        List<Article> articleList = articleRepository.findByContentContaining(keyword);
        return ArticleListResponse.createResponse(articleList);
    }
}
