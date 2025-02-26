package performance.lab2.dto;

import java.util.List;

public record TeamWithMembersDto(
        Long teamId,
        String name,
        List<MemberDto> members
) {
}
