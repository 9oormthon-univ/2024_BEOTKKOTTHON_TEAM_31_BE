package goorm.brainsnack.member.domain;

import goorm.brainsnack.global.BaseEntity;
import goorm.brainsnack.member.dto.MemberResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "Member_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String temporaryId;

    //생성 메서드
    public static Member from(String temporaryId) {
        return Member.builder()
                .temporaryId(temporaryId)
                .build();
    }

    public static MemberResponseDto.LoginDto toMemberRequestDto(Member member) {
        return MemberResponseDto.LoginDto.builder()
                .id(member.id)
                .entryCode(member.temporaryId)
                .build();
    }

    public static MemberResponseDto.MemberDto toMemberDto(Member member) {
        return MemberResponseDto.MemberDto.builder()
                .id(member.id)
                .entryCode(member.temporaryId)
                .build();
    }
}
