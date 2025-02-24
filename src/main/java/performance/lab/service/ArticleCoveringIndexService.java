package performance.lab.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import performance.lab.domain.Article;
import performance.lab.dto.ArticlePageResponse;
import performance.lab.dto.ArticleTtitleLikeCountResponse;
import performance.lab2.monitor.LogExecutionTime;
import performance.lab.repository.ArticleCoveringIndexRepository;

@Service
@RequiredArgsConstructor
public class ArticleCoveringIndexService {
    private final ArticleCoveringIndexRepository articleCoveringIndexRepository;

    /**
     * title,likeCount 복합인덱스에 대한 커버링 인덱스 조회
     */
    @LogExecutionTime
    public List<ArticleTtitleLikeCountResponse> findArticleByTitleAndLikeCount(String title) {
        return articleCoveringIndexRepository.findByTitle(title);
    }

    /**
     * 일반 페이징 조회
     */
    @LogExecutionTime
    public ArticlePageResponse findArticleWithPaging(String title, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Article> articlePage = articleCoveringIndexRepository.findByTitleWithPaging(title, pageRequest);
        return ArticlePageResponse.from(articlePage);
    }

    /**
     * 커버링 인덱스 활용한 페이징 조회
     */
    @LogExecutionTime
    public ArticlePageResponse findArticleWithPagingAndCoveringIndex(String title, int page, int size) {
        int offset = page * size;
        List<Article> articles = articleCoveringIndexRepository.findByTitleWithPagingAndCoveringIndex(title, offset,
                size);
        return ArticlePageResponse.of(articles, page, size);
    }
}
