package performance.lab.dto;

import java.util.List;
import org.springframework.data.domain.Page;
import performance.lab.domain.Article;

public record ArticlePageResponse(
        List<ArticleResponse> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {
    public static ArticlePageResponse from(Page<Article> page) {
        return new ArticlePageResponse(
                page.getContent().stream()
                        .map(ArticleResponse::from)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    public static ArticlePageResponse of(List<Article> content, int pageNumber, int pageSize) {
        return new ArticlePageResponse(
                content.stream()
                        .map(ArticleResponse::from)
                        .toList(),
                pageNumber,
                pageSize,
                content.size(),
                1,
                true
        );
    }
}
