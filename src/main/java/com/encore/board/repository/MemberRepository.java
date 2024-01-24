package com.encore.board.repository;

import com.encore.board.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // findBy컬럼명의 규칙으로 자동으로 where 조건문을 사용한 메서드 생성
    Optional<Member> findByEmail(String email);
}
