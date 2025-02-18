package performance.lab.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import performance.lab.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.user.userId = :userId")
    List<Article> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM Article a WHERE a.content LIKE %:keyword%")
    List<Article> findByContentContaining(@Param("keyword") String keyword);
}
