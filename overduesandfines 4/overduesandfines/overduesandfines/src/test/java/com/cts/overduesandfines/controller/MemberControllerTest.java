package com.cts.overduesandfines.controller;

import com.cts.overduesandfines.model.Member;
import com.cts.overduesandfines.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        member.setMemberId("M001");
    }

    @Test
    void getMemberById_Success() throws Exception {
        when(memberService.getMemberById(1L)).thenReturn(member);

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.memberId").value("M001"));

        verify(memberService).getMemberById(1L);
    }

    @Test
    void getMemberByMemberId_Success() throws Exception {
        when(memberService.getMemberByMemberId("M001")).thenReturn(member);

        mockMvc.perform(get("/api/members/member-id/M001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.memberId").value("M001"));

        verify(memberService).getMemberByMemberId("M001");
    }
}