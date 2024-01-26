package com.encore.board.entity;

import com.encore.board.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

    public Post(String title, String contents, Member member){
        this.title = title;
        this.contents = contents;
        this.member = member;
        // Member 객체의 posts를 초기화 시켜준 후
//        this.member.getPosts().add(this);
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 3000, nullable = false)
    private String contents;

    @Column
    private String appointment;

    @Column
    private LocalDateTime appointmentTime;

    // member_id는 DB의 컬럼명, 별도의 옵션 없을 시 member의 PK에 FK가 설정
    @JoinColumn(name = "member_id")
    // JPA에게 Author와 Post의 관계를 알려줌. Post 객체 입장에서 한 사람이 여러 개 글을 쓸 수 있으므로 N:1
    @ManyToOne(fetch = FetchType.LAZY) // 참조하기 전까지는 Author Table과 Left Join 실행 X, 디폴트는 FectchType.Eager임
    // @JoinColumn(nullable = false, name = "member_email", referencedColumnName = "email")
    private Member member;

    public void updateTitleContents(String title, String contents){
        this.title = title;
        this.contents = contents;
    }

    public void updateAppointment(String appointment){
        this.appointment = appointment;
    }
}
