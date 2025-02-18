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

    /**
     * 기본 조회
     */
    public ArticleListResponse findArticle(Long userId) {
        List<Article> articleList = articleRepository.findByUserId(userId);

        return ArticleListResponse.createResponse(articleList);
    }

    /**
     * content 인덱스 적용 전 조회
     */
    @LogExecutionTime
    public ArticleListResponse findArticleByContent(String keyword) {
        List<Article> articleList = articleRepository.findByContentContaining(keyword);
        return ArticleListResponse.createResponse(articleList);
    }

    /**
     * content 인덱스 적용 후 content 조회
     */
    @LogExecutionTime
    public ArticleListResponse findArticleByContentWithIndexing(String keyword) {
        List<Article> articleList = articleRepository.findByContentContainingWithIndexing(keyword);
        return ArticleListResponse.createResponse(articleList);
    }

    /**
     * content 커버링 인덱스를 통한 content 조회
     */
    @LogExecutionTime
    public ArticleListResponse findArticleByContentWithCoveringIndexing(String keyword) {
        // 1. 먼저 ID만 조회 (인덱스만 사용)
        List<Long> articleIds = articleRepository.findArticleIdsByContent(keyword);

        // 2. 조회된 ID로 실제 데이터 조회
        List<Article> articles = articleRepository.findByIds(articleIds);

        return ArticleListResponse.createResponse(articles);
    }

    /**
     * 선택도 이슈 해결을 위해 title 조회로 변경
     * title 인덱스 적용 후 title 조회
     */
    @LogExecutionTime
    public ArticleListResponse findArticleByTitleWithIndexing(String keyword) {
        List<Article> articleList = articleRepository.findByTitleContainingWithIndexing(keyword);
        return ArticleListResponse.createResponse(articleList);
    }
}
