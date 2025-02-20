package performance.lab.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import performance.lab.domain.Article;

@Repository
public interface ArticleCompositeIndexRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.title LIKE :title% AND a.likeCount > 20")
    List<Article> findByTitleAndLikeCount(@Param("title") String title);

    @Query("SELECT a FROM Article a WHERE a.title LIKE :title% AND a.likeCount > 98")
    List<Article> findByTitleAndLikeCountWithCompositeIndex(@Param("title") String title);

    @Query("SELECT a FROM Article a WHERE a.title LIKE :title% AND a.likeCount > 98")
    List<Article> findWithReverseCompositeIndex(@Param("title") String title);
}
