package performance.lab2.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import performance.lab2.domain.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select t from Team t")
    List<Team> findAllTeam();

    @Query("select distinct t from Team t join fetch t.members")
    List<Team> findAllTeamWithMembers();

    @EntityGraph(attributePaths = {"members"})
    @Query("select t from Team t")
    List<Team> findAllTeamWithEntityGraph();

    // 또는 메소드 이름으로 쿼리 생성 시에도 적용 가능
    @EntityGraph(attributePaths = {"members"})
    List<Team> findAll();
}
