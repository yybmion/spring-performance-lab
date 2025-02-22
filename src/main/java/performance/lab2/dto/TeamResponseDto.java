package performance.lab2.dto;

import java.util.List;
import java.util.stream.Collectors;
import performance.lab2.domain.Team;

public record TeamResponseDto(
        Long teamId,
        String name,
        List<MemberResponseDto> members
) {
    public static TeamResponseDto of(Team team) {
        return new TeamResponseDto(
                team.getTeamId(),
                team.getName(),
                team.getMembers().stream()
                        .map(MemberResponseDto::of)
                        .collect(Collectors.toList())
        );
    }
}
