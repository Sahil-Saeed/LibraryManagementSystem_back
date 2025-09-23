package com.cts.overduesandfines.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member();
    }

    @Test
    void testMemberCreation() {
        member.setId(1L);
        member.setName("John Doe");
        member.setMemberId("M001");

        assertEquals(1L, member.getId());
        assertEquals("John Doe", member.getName());
        assertEquals("M001", member.getMemberId());
    }

    @Test
    void testMemberSettersAndGetters() {
        member.setName("Jane Smith");
        member.setMemberId("M002");

        assertNotNull(member.getName());
        assertNotNull(member.getMemberId());
        assertEquals("Jane Smith", member.getName());
        assertEquals("M002", member.getMemberId());
    }
}