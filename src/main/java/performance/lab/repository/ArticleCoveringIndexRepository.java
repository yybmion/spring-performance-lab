package performance.lab.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import performance.lab.domain.Article;
import performance.lab.dto.ArticleTtitleLikeCountResponse;

@Repository
public interface ArticleCoveringIndexRepository extends JpaRepository<Article, Long> {
    @Query("SELECT new performance.lab.dto.ArticleTtitleLikeCountResponse(a.title, a.likeCount) " +
            "FROM Article a WHERE a.title LIKE :title%")
    List<ArticleTtitleLikeCountResponse> findByTitle(@Param("title") String title);

    // 일반 페이징 쿼리
    @Query("SELECT a FROM Article a JOIN a.user u WHERE a.title LIKE :title% ORDER BY a.articleId DESC")
    Page<Article> findByTitleWithPaging(@Param("title") String title, Pageable pageable);

    // 커버링 인덱스 페이징 쿼리
    @Query(value = """
            SELECT a.* 
            FROM (
                SELECT article_id 
                FROM article 
                WHERE title LIKE %:title% 
                ORDER BY article_id DESC
                LIMIT :offset, :limit
            ) b 
            JOIN article a ON a.article_id = b.article_id
            """,
            nativeQuery = true)
    List<Article> findByTitleWithPagingAndCoveringIndex(
            @Param("title") String title,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
}
