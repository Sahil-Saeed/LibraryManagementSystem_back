package com.cts.overduesandfines.service;

import com.cts.overduesandfines.client.MemberClient;
import com.cts.overduesandfines.dto.MemberDto;
import com.cts.overduesandfines.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberClient memberClient;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private MemberDto memberDto;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        member.setMemberId("M001");
        
        memberDto = new MemberDto();
        memberDto.setId(1L);
        memberDto.setName("John Doe");
        memberDto.setMemberId("M001");
    }

    @Test
    void getMemberById_Success() {
        when(memberClient.getMemberById(1L)).thenReturn(memberDto);

        Member result = memberService.getMemberById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("M001", result.getMemberId());
        verify(memberClient).getMemberById(1L);
    }

    @Test
    void getMemberById_NotFound_ReturnsNull() {
        when(memberClient.getMemberById(99L)).thenReturn(null);

        Member result = memberService.getMemberById(99L);

        assertNull(result);
        verify(memberClient).getMemberById(99L);
    }

    @Test
    void getMemberByMemberId_Success() {
        when(memberClient.getMemberByMemberId("M001")).thenReturn(memberDto);

        Member result = memberService.getMemberByMemberId("M001");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("M001", result.getMemberId());
        verify(memberClient).getMemberByMemberId("M001");
    }

    @Test
    void getMemberByMemberId_NotFound_ReturnsNull() {
        when(memberClient.getMemberByMemberId("INVALID")).thenReturn(null);

        Member result = memberService.getMemberByMemberId("INVALID");

        assertNull(result);
        verify(memberClient).getMemberByMemberId("INVALID");
    }
}