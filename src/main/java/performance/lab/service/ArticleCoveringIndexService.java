package performance.lab.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import performance.lab.dto.ArticleTtitleLikeCountResponse;
import performance.lab.monitor.LogExecutionTime;
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
}
