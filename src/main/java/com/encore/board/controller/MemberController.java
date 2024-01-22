package com.encore.board.controller;

import com.encore.board.dto.request.MemberSignUpRequest;
import com.encore.board.dto.response.MemberDetailsResponse;
import com.encore.board.dto.response.MemberSignUpResponse;
import com.encore.board.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/members")
@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping("/sign-up")
    @ResponseBody
    public String signUp(@RequestBody MemberSignUpRequest req){
        MemberSignUpResponse result = memberService.signup(req);
        return result.getNickname() + "님 환영합니다.";
    }

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

        // ResponseEntity<MemberDetailsResponse> 적용
//        try {
//            MemberDetailsResponse result = memberService.showMemberDetails(id);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (MemberNotExistException e){
//            log.info(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }



    @GetMapping("/check-duplicate")
    @ResponseBody
    public boolean duplicationCheck(String email){
        return memberService.checkDuplicate(email);
    }
}
