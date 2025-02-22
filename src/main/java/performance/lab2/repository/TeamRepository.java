package performance.lab2.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import performance.lab2.domain.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select t from Team t")
    List<Team> findAllTeam();
}
