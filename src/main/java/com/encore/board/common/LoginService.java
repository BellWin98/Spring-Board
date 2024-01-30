package com.encore.board.common;

import com.encore.board.entity.Member;
import com.encore.board.exception.member.MemberNotExistException;
import com.encore.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 스프링에서 매개변수인 username에 로그인 창에서 입력한 email을 넣어줄거임
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(username)
                .orElseThrow(MemberNotExistException::new);

        // 권한을 여러개 받을 수 있으므로, List 선언
        List<GrantedAuthority> authorities = new ArrayList<>();
        // ROLE_권한 -> 이 패턴으로 스프링에서 기본적으로 권한 체크
        authorities.add(new SimpleGrantedAuthority(findMember.getRole().toString()));

        // 매개변수: userEmail, userPassword, 권한(Authorities)
        // 해당 메서드에서 Return되는 User 객체는 Session 메모리 저장소에 저장되어, 계속 사용 가능
        // Authentication 객체에 사용자 정보 저장
        return new User(findMember.getEmail(), findMember.getPassword(), authorities);
    }
}
