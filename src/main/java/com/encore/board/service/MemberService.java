package com.encore.board.service;

import com.encore.board.dto.request.MemberSignUpRequest;
import com.encore.board.dto.request.MemberUpdateRequest;
import com.encore.board.dto.response.MemberDetailsResponse;
import com.encore.board.dto.response.MemberSimpleInfoResponse;
import com.encore.board.dto.response.MyPostResponse;
import com.encore.board.entity.Member;
import com.encore.board.entity.Post;
import com.encore.board.entity.Role;
import com.encore.board.exception.member.MemberEmailAlreadyExistException;
import com.encore.board.exception.member.MemberNotExistException;
import com.encore.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.encore.board.entity.Role.ROLE_USER;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(MemberSignUpRequest req){
        if (memberRepository.findByEmail(req.getEmail()).isPresent()){
            throw new MemberEmailAlreadyExistException();
        }
        Role role;
        if (req.getRole().equals("admin")){
            role = Role.ROLE_ADMIN;
        } else {
            role = ROLE_USER;
        }

        Member member = Member.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .role(role)
                .build();

        // CASCADE.PERSIST 테스트
        // 부모 테이블을 통해 자식 테이블에 동시에 객체 생성
//        List<Post> posts = new ArrayList<>();
//        Post post = Post.builder()
//                .title("안녕하세요 " + member.getNickname() + "입니다.")
//                .contents("제 이메일은 " + member.getEmail() + "입니다.")
//                .member(member)
//                .build();
//        posts.add(post);
//        member.setPosts(posts);

        memberRepository.save(member);
    }

//    @Transactional
    public List<MemberSimpleInfoResponse> showMembers(){
        List<Member> members = memberRepository.findAll();
        List<MemberSimpleInfoResponse> memberSimpleInfoResponseList = new ArrayList<>();
        for (Member member : members){
            MemberSimpleInfoResponse memberSimpleInfoResponse = MemberSimpleInfoResponse.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .build();
            memberSimpleInfoResponseList.add(memberSimpleInfoResponse);
        }

        return memberSimpleInfoResponseList;
    }

//    @Transactional
    public MemberDetailsResponse showMemberDetails(Long id) throws MemberNotExistException{
        Member member = memberRepository.findById(id).orElseThrow(MemberNotExistException::new);
        String role;
        if (member.getRole() == null || member.getRole().equals(ROLE_USER)){
            role = "일반 사용자";
        } else {
            role = "관리자";
        }

        return MemberDetailsResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .createdTime(member.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .postCount(member.getPosts().size())
                .role(role)
                .build();
    }

    @Transactional
    public void update(Long id, MemberUpdateRequest req) throws MemberNotExistException{
        Member member = memberRepository.findById(id).orElseThrow(MemberNotExistException::new);
        member.updateMemberInfo(req.getPassword(), req.getNickname());
        // 명시적으로 save를 하지 않더라도 JPA의 영속성 컨텍스트를 통해,
        // 객체의 변경이 감지(Dirty Checking)되면 트랜잭션이 완료되는 시점에 save 동작.
//         memberRepository.save(member);
    }

    public void delete(Long id){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotExistException::new);
        memberRepository.delete(member);
    }

    public List<MyPostResponse> getMyPosts(Long id){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotExistException::new);
        List<MyPostResponse> myPosts = new ArrayList<>();
        for (Post post : member.getPosts()){
            MyPostResponse myPostResponse = MyPostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .build();
            myPosts.add(myPostResponse);
        }
        return myPosts;
    }

    public Member circleTest(Long id){
        return memberRepository.findById(id).orElseThrow(MemberNotExistException::new);
    }

    public boolean checkDuplicate(String email){
        Optional<Member> findEmail = memberRepository.findByEmail(email);
        return findEmail.isPresent();
    }
}
