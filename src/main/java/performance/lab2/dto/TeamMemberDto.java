package performance.lab2.dto;

public record TeamMemberDto(
        Long teamId,
        String teamName,
        Long memberId,
        String memberName
) {
}

