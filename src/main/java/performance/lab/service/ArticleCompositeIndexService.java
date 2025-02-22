package performance.lab.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import performance.lab.domain.Article;
import performance.lab.dto.ArticleListResponse;
import performance.lab2.monitor.LogExecutionTime;
import performance.lab.repository.ArticleCompositeIndexRepository;

@Service
@RequiredArgsConstructor
public class ArticleCompositeIndexService {

    private final ArticleCompositeIndexRepository articleCompositeIndexRepository;

    /**
     * title만 인덱스 적용했을 때, likeCount 조건 쿼리 추가
     */
    @LogExecutionTime
    public ArticleListResponse findArticleByTitleAndLikeCount(String title) {
        List<Article> articleList = articleCompositeIndexRepository.findByTitleAndLikeCount(title);
        return ArticleListResponse.createResponse(articleList);
    }

    /**
     * title,likeCount 복합 인덱스 생성
     */
    @LogExecutionTime
    public ArticleListResponse findArticleByTitleAndLikeCountWithCompositeIndex(String title) {
        List<Article> articleList = articleCompositeIndexRepository.findByTitleAndLikeCountWithCompositeIndex(title);
        return ArticleListResponse.createResponse(articleList);
    }

    /**
     * likeCount,title 복합 인덱스 생성
     */
    @LogExecutionTime
    public ArticleListResponse findArticleWithReverseCompositeIndex(String title) {
        List<Article> articleList = articleCompositeIndexRepository.findWithReverseCompositeIndex(title);
        return ArticleListResponse.createResponse(articleList);
    }
}
