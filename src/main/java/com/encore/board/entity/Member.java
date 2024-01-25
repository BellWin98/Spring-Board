package com.encore.board.entity;

import com.encore.board.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
// Builder 어노테이션을 통해 빌터 패턴으로 객체 생성
// 매개변수의 세팅 순서, 매개변수 개수 등을 유연하게 세팅
@SuperBuilder
@AllArgsConstructor
// 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
@NoArgsConstructor // (access = AccessLevel.PROTECTED)
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

    // mappedBy에 연관 관계의 주인(Post: FK를 관리하는 객체)을 명시하고, FK를 관리하는 변수명(Post의 member변수)을 명시
    // DB 차원에서 Member에 Post와 관련한 필드가 없기 때문에, 명시해줘야함
    // Cascade: 부모 테이블에 변경이 생겼을 때, 자식 테이블에도 영향이 있음
    // MemberRepository에 Member를 save 하는 순간 Cascade.Persist에 의해 Post 테이블에 post 객체가 자동 생성됨
    // 1:1 관계일 경우 @OneToOne도 존재
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    // Member 조회 시 Post 객체가 필요할 때 선언
    // 필요 없으면 안쓰는게 나음. Member 객체를 JSON으로 직렬화 하는 과정에서 순환 참조 문제 발생
    // 해결: Dto로 풀어주자
    // @Setter // cascade.persist를 위한 테스트
    private List<Post> posts;

    public void updateMemberInfo(String password, String nickname){
        this.password = password;
        this.nickname = nickname;
    }
}
