package goorm.brainsnack.member.domain;

import goorm.brainsnack.global.BaseEntity;
import goorm.brainsnack.member.dto.MemberResponseDto;
import jakarta.persistence.*;
import lombok.*;

import static goorm.brainsnack.member.dto.MemberResponseDto.*;

@Getter
@Builder
@Entity
@Table(name = "Member_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String nickname;

    //생성 메서드
    public static Member from(String nickname) {
        return Member.builder()
                .nickname(nickname)
                .build();
    }

    public static LoginDto toMemberRequestDto(Member member) {
        return LoginDto.builder()
                .id(member.id)
                .nickname(member.nickname)
                .build();
    }
}
