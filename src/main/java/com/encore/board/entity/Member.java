package com.encore.board.entity;

import com.encore.board.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
// 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void updateMemberInfo(String password, String nickname){
        this.password = password;
        this.nickname = nickname;
    }
}
