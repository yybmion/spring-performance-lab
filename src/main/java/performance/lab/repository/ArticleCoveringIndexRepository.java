package performance.lab.repository;

import java.util.List;
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
}
