package com.encore.board.controller;

import com.encore.board.dto.request.MemberSignUpRequest;
import com.encore.board.dto.request.MemberUpdateRequest;
import com.encore.board.dto.response.MemberDetailsResponse;
import com.encore.board.dto.response.MyPostResponse;
import com.encore.board.entity.Member;
import com.encore.board.exception.member.MemberEmailAlreadyExistException;
import com.encore.board.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/members")
@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/sign-up")
    public String signUp(){
        return "member/member-create";
    }

    @GetMapping("/login-page")
    public String login(){
        return "member/login-page";
    }

    @PostMapping("/sign-up")
    public String signUp(Model model, MemberSignUpRequest req){
        try{
            memberService.signup(req);
            return "redirect:member-list";
        } catch (MemberEmailAlreadyExistException e){
            model.addAttribute("errorMessage", e.getMessage());
            log.error(e.getMessage());
            return "member/member-create";
        }
    }

    @PreAuthorize("hasRole('ADMIN')") // 권한 검사 (관리자 권한이 아니면 조회 불가)
    @GetMapping("/member-list")
    public String memberList(Model model){
        model.addAttribute("members", memberService.showMembers());
        return "member/member-list";
    }

    @GetMapping("/detail/{id}")
//    @ResponseBody
    public String memberDetails(@PathVariable Long id, Model model) {
        MemberDetailsResponse result = memberService.showMemberDetails(id);
        model.addAttribute("member", result);
        return "member/member-details";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, MemberUpdateRequest req){
        memberService.update(id, req);
        return "redirect:/api/members/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        memberService.delete(id);
        return "redirect:/api/members/member-list";
    }

    @GetMapping("/detail/{id}/myPosts")
    public String getMyPosts(@PathVariable Long id, Model model) {
        List<MyPostResponse> result = memberService.getMyPosts(id);
        model.addAttribute("myPosts", result);
        return "member/my-post";
    }
        // ResponseEntity<MemberDetailsResponse> 적용
//        try {
//            MemberDetailsResponse result = memberService.showMemberDetails(id);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (MemberNotExistException e){
//            log.info(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }

    @GetMapping("/{id}/circle/entity")
    @ResponseBody
    // 연관관계가 있는 Member 엔티티를 Json으로 직렬화 하게 될 경우,
    // 순환 참조 이슈 발생하므로, DTO 사용 필요
    public Member circleIssueTest1(@PathVariable Long id){
        return memberService.circleTest(id);
    }

    @GetMapping("/{id}/circle/dto")
    @ResponseBody
    public MemberDetailsResponse circleIssueTest2(@PathVariable Long id){
        return memberService.showMemberDetails(id);
    }

    @GetMapping("/check-duplicate")
    @ResponseBody
    public boolean duplicationCheck(String email){
        return memberService.checkDuplicate(email);
    }
}
