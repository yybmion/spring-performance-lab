package performance.lab2.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import performance.lab2.domain.Team;
import performance.lab2.dto.TeamMemberDto;

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

    // DTO로 직접 조회하는 메소드
    @Query("select new performance.lab2.dto.TeamMemberDto(t.teamId, t.name, m.memberId, m.name) " +
            "from Team t join t.members m")
    List<TeamMemberDto> findAllTeamsWithMembersDto();

    // 멤버가 없는 팀도 포함하려면 left join 사용
    @Query("select new performance.lab2.dto.TeamMemberDto(t.teamId, t.name, m.memberId, m.name) " +
            "from Team t left join t.members m")
    List<TeamMemberDto> findAllTeamsWithMembersDtoLeftJoin();
}
