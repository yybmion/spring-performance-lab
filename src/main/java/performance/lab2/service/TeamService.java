package performance.lab2.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import performance.lab2.domain.Team;
import performance.lab2.dto.TeamResponseDto;
import performance.lab2.monitor.LogExecutionTime;
import performance.lab2.repository.TeamRepository;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teamRepository;

    /**
     * N+1 문제 (일반 조회)
     */
    @LogExecutionTime
    public List<TeamResponseDto> findTeams() {
        AtomicInteger queryCount = new AtomicInteger(0);

        List<Team> teams = teamRepository.findAllTeam();
        queryCount.incrementAndGet();

        List<TeamResponseDto> result = teams.stream()
                .map(team -> {
                    team.getMembers().size();
                    queryCount.incrementAndGet();
                    return TeamResponseDto.of(team);
                })
                .collect(Collectors.toList());

        log.info("일반 조회 총 쿼리 수: {}", queryCount.get());
        return result;
    }

    /**
     * fetch join 적용
     */
    @LogExecutionTime
    public List<TeamResponseDto> findTeamsWithMembers() {
        AtomicInteger queryCount = new AtomicInteger(0);

        List<Team> teams = teamRepository.findAllTeamWithMembers();
        queryCount.incrementAndGet();

        List<TeamResponseDto> result = teams.stream()
                .map(team -> TeamResponseDto.create(team, team.getMembers()))
                .collect(Collectors.toList());

        log.info("Fetch Join 총 쿼리 수: {}", queryCount.get());
        return result;
    }

    /**
     * EntityGraph 적용
     */
    @LogExecutionTime
    public List<TeamResponseDto> findTeamsWithEntityGraph() {
        AtomicInteger queryCount = new AtomicInteger(0);

        List<Team> teams = teamRepository.findAllTeamWithEntityGraph();
        queryCount.incrementAndGet();

        List<TeamResponseDto> result = teams.stream()
                .map(TeamResponseDto::of)  // TeamResponseDto.of() 메소드 직접 참조
                .collect(Collectors.toList());

        log.info("EntityGraph 총 쿼리 수: {}", queryCount.get());
        return result;
    }
}
