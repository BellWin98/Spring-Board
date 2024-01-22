package com.encore.board.service;

import com.encore.board.dto.request.MemberSignUpRequest;
import com.encore.board.dto.response.MemberDetailsResponse;
import com.encore.board.dto.response.MemberSignUpResponse;
import com.encore.board.dto.response.MemberSimpleInfoResponse;
import com.encore.board.entity.Member;
import com.encore.board.entity.Role;
import com.encore.board.exception.member.MemberNotExistException;
import com.encore.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

//    @Transactional
    public MemberSignUpResponse signup(MemberSignUpRequest req){
        Member member = Member.builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .nickname(req.getNickname())
                .role(Role.ROLE_USER)
                .build();
        memberRepository.save(member);

        return MemberSignUpResponse.builder()
                .nickname(member.getNickname())
                .build();
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

        return MemberDetailsResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .createdTime(member.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public boolean checkDuplicate(String email){
        Optional<Member> findEmail = memberRepository.findByEmail(email);
        return findEmail.isPresent();
    }
}
