package com.cts.lms.service;

import com.cts.lms.dto.*;

public interface IMemberService {
    MemberResponseDto register(MemberRegistrationDto dto);
    MemberResponseDto authenticate(MemberLoginDto dto);
    MemberResponseDto updateProfile(Long id, MemberUpdateDto dto);
    MemberResponseDto getMemberById(Long id);
    void deleteMember(Long id);
    java.util.List<MemberResponseDto> getAllMembers();
}