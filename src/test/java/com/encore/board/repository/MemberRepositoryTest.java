package com.encore.board.repository;

import com.encore.board.entity.Member;
import com.encore.board.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

// DataJpaTest 어노테이션을 사용하면 매 테스트 종료 시 자동으로 DB 원상 복구
// DB에 데이터 넣고 Rollback까지 해야함.
// 모든 스프링 빈을 생성하지 않고, Respository(DB) 테스트 특화 어노테이션
 @DataJpaTest
 // replace = AutoConfigureTestDatabase.Replace.ANY가 디폴트 값: H2DB(spring 내장 인메모리)가 기본 설정
 @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // yml에 있는 DB를 갖다 쓰겠다.
 // @ActiveProfiles("test") // application-test.yml 파일을 찾아 설정값 세팅

// 이 어노테이션은 자동 롤백 기능을 지원하지 않고, 별도로 롤백 코드 필요
// 또한, 실제 스프링 실행과 동일하게 스프링 빈 생성 및 주입
//@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Test
    public void memberSaveTest(){
        // 로직: 객체 생성 -> Save -> 재조회해서 방금 생성한 객체와 비교
        // 준비(prepare, given) 단계: 데이터 준비
        Member member = Member.builder()
                .email("luis3@naver.com")
                .password("luis1234@")
                .nickname("luis3")
                .role(Role.ROLE_ADMIN)
                .build();
        // 실행(execute, when) 단계:
        memberRepository.save(member);
        Member dbMember = memberRepository.findByEmail("luis3@naver.com").orElse(null);
        // 검증(then) 단계
        // Assertions 클래스의 기능을 통해 오류의 원인파악, null처리, 가시적으로 성공/실패 여부 확인
        assertEquals(member.getEmail(), dbMember.getEmail());
    }
}
