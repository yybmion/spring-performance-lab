package performance.lab2.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import performance.lab2.domain.Team;
import performance.lab2.dto.TeamResponseDto;
import performance.lab2.monitor.LogExecutionTime;
import performance.lab2.repository.TeamRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    private final TeamRepository teamRepository;

    @LogExecutionTime
    public List<TeamResponseDto> findTeams() {
        List<Team> teams = teamRepository.findAllTeam();
        return teams.stream()
                .map(TeamResponseDto::of)
                .collect(Collectors.toList());
    }
}
