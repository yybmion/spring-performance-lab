package performance.lab2.dto;

import performance.lab2.domain.Member;

public record MemberResponseDto(
        Long memberId,
        String name
) {
    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(
                member.getMemberId(),
                member.getName()
        );
    }
}
