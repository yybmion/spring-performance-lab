package performance.lab2.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import performance.lab2.domain.Team;
import performance.lab2.dto.MemberDto;
import performance.lab2.dto.TeamMemberDto;
import performance.lab2.dto.TeamResponseDto;
import performance.lab2.dto.TeamWithMembersDto;
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

    /**
     * DTO 직접 조회 적용
     */
    @LogExecutionTime
    public List<TeamWithMembersDto> findTeamsWithMembersDirectDto() {
        AtomicInteger queryCount = new AtomicInteger(0);

        // DTO로 직접 조회
        List<TeamMemberDto> flatResults = teamRepository.findAllTeamsWithMembersDto();
        queryCount.incrementAndGet();

        // 평면적 결과를 계층형 구조로 변환
        Map<Long, TeamWithMembersDto> teamMap = new LinkedHashMap<>();

        for (TeamMemberDto dto : flatResults) {
            teamMap.computeIfAbsent(
                    dto.teamId(),
                    id -> new TeamWithMembersDto(dto.teamId(), dto.teamName(), new ArrayList<>())
            );

            // 현재 팀에 멤버 추가
            TeamWithMembersDto teamDto = teamMap.get(dto.teamId());
            if (dto.memberId() != null) {  // null 체크 (left join 사용 시 필요)
                teamDto.members().add(new MemberDto(dto.memberId(), dto.memberName()));
            }
        }

        List<TeamWithMembersDto> result = new ArrayList<>(teamMap.values());

        log.info("DTO 직접 조회 총 쿼리 수: {}", queryCount.get());
        return result;
    }

    /**
     * DTO 직접 조회 + Left Join 적용 (멤버가 없는 팀도 포함)
     */
    @LogExecutionTime
    public List<TeamWithMembersDto> findAllTeamsWithMembersDirectDtoLeftJoin() {
        AtomicInteger queryCount = new AtomicInteger(0);

        List<TeamMemberDto> flatResults = teamRepository.findAllTeamsWithMembersDtoLeftJoin();
        queryCount.incrementAndGet();

        // 변환 로직은 동일
        Map<Long, TeamWithMembersDto> teamMap = new LinkedHashMap<>();

        for (TeamMemberDto dto : flatResults) {
            teamMap.computeIfAbsent(
                    dto.teamId(),
                    id -> new TeamWithMembersDto(dto.teamId(), dto.teamName(), new ArrayList<>())
            );

            // memberId가 null이 아닌 경우에만 멤버 추가 (left join일 경우 null 가능)
            if (dto.memberId() != null) {
                TeamWithMembersDto teamDto = teamMap.get(dto.teamId());
                teamDto.members().add(new MemberDto(dto.memberId(), dto.memberName()));
            }
        }

        List<TeamWithMembersDto> result = new ArrayList<>(teamMap.values());

        log.info("DTO 직접 조회 (Left Join) 총 쿼리 수: {}", queryCount.get());
        return result;
    }
}
