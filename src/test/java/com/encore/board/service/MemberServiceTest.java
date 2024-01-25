package com.encore.board.service;

import com.encore.board.dto.request.MemberUpdateRequest;
import com.encore.board.dto.response.MemberDetailsResponse;
import com.encore.board.entity.Member;
import com.encore.board.entity.Post;
import com.encore.board.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    // 진짜 객체
    @Autowired
    private MemberService memberService;

    // 가짜 객체를 만드는 작업을 Mocking이라 한다.
    @MockBean
    private MemberRepository memberRepository;

    @Test
    void findAllTest(){

        // Mock Repository 기능 구현
        List<Member> members = new ArrayList<>();
        members.add(new Member());
        members.add(new Member());
        // 가짜 repository에서 findAll을 실행하면 항상 2개의 member 객체만 return 하도록 설정
        Mockito.when(memberRepository.findAll()).thenReturn(members);

        // 검증
        Assertions.assertEquals(2, memberService.showMembers().size());
    }

    @Test
    void updateTest(){
        Long memberId = 1L;
        Member member = Member.builder()
                .nickname("test1")
                .email("test1@naver.com")
                .password("1224")
                .build();
        // 아래 코드에 의해 member의 메모리 주소 값이 memberService.update()함수의 member로 전달
        Mockito.when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
        memberUpdateRequest.setNickname("test2");
        memberUpdateRequest.setPassword("4321");
        memberService.update(memberId, memberUpdateRequest);

        Assertions.assertEquals(member.getNickname(), memberUpdateRequest.getNickname());
    }

    @Test
    void showMemberDetailsTest(){
        Long memberId = 1L;
        List<Post> posts = new ArrayList<>();
        Post post = Post.builder()
                .title("title1")
                .contents("contents1")
                .build();
        posts.add(post);
        Member member = Member.builder()
                .nickname("test1")
                .email("test1@naver.com")
                .password("1224")
                .posts(posts)
                .createdTime(LocalDateTime.now())
                .build();
        // 아래 코드에 의해 member의 메모리 주소 값이 memberService.update()함수의 member로 전달
        Mockito.when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        MemberDetailsResponse memberDetailsResponse = memberService.showMemberDetails(memberId);
        Assertions.assertEquals(member.getNickname(), memberDetailsResponse.getNickname());
        Assertions.assertEquals(member.getPosts().size(), memberDetailsResponse.getPostCount());
        Assertions.assertEquals("관리자", memberDetailsResponse.getRole());
    }
}
