package performance.lab.dto;

import java.util.List;
import java.util.stream.Collectors;
import performance.lab.domain.Article;

public record ArticleListResponse(
        List<ArticleResponse> article
) {
    public static ArticleListResponse createResponse(List<Article> list) {
        List<ArticleResponse> responses = list.stream()
                .map(ArticleResponse::from)
                .collect(Collectors.toList());

        return new ArticleListResponse(responses);
    }
}
