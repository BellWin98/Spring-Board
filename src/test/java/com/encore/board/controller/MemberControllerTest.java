package com.encore.board.controller;

import com.encore.board.dto.response.MemberDetailsResponse;
import com.encore.board.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// WebMvcTest를 이용해서 Controller 계층을 테스트. 모든 스프링 빈을 생성하고 주입하지는 않음
//@WebMvcTest(MemberController.class)

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
//    @WithMockUser // security 의존성 추가 필요
    void memberDetailsTest() throws Exception {
        MemberDetailsResponse memberDetailsResponse = MemberDetailsResponse.builder()
                .nickname("test nickname")
                .email("test@naver.com")
                .build();
        Mockito.when(memberService.showMemberDetails(1L)).thenReturn(memberDetailsResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/members/1/circle/dto"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name", memberDetailsResponse.getNickname()));
    }
}
