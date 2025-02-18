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

    @Query("SELECT a FROM Article a WHERE a.content = :keyword")
    List<Article> findByContentContainingWithIndexing(@Param("keyword") String keyword);

    // 1단계: 인덱스만 사용하여 ID 조회
    @Query("SELECT a.articleId FROM Article a WHERE a.content = :keyword")
    List<Long> findArticleIdsByContent(@Param("keyword") String keyword);

    // 2단계: 조회된 ID로 실제 데이터 조회
    @Query("SELECT a FROM Article a WHERE a.articleId IN :ids")
    List<Article> findByIds(@Param("ids") List<Long> ids);

    @Query("SELECT a FROM Article a WHERE a.title LIKE :keyword%")
    List<Article> findByTitleContainingWithIndexing(@Param("keyword") String keyword);
}
