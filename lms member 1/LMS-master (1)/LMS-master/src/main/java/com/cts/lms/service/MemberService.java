package com.cts.lms.service;

import com.cts.lms.dto.*;
import com.cts.lms.entity.Member;
import com.cts.lms.exception.*;
import com.cts.lms.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService implements IMemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MemberResponseDto register(MemberRegistrationDto dto) {
        try {
            if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new MemberValidationException("Email already exists");
            }
            Member member = modelMapper.map(dto, Member.class);
            Member saved = memberRepository.save(member);
            return modelMapper.map(saved, MemberResponseDto.class);
        } catch (Exception e) {
            if (e instanceof MemberValidationException) {
                throw e;
            }
            throw new MemberServiceException("Failed to register member", e);
        }
    }

    @Override
    public MemberResponseDto authenticate(MemberLoginDto dto) {
        try {
            Optional<Member> memberOpt = memberRepository.findByEmail(dto.getEmail());
            if (memberOpt.isPresent() && memberOpt.get().getPassword().equals(dto.getPassword())) {
                return modelMapper.map(memberOpt.get(), MemberResponseDto.class);
            }
            throw new SecurityException("Invalid credentials");
        } catch (Exception e) {
            if (e instanceof SecurityException) {
                throw e;
            }
            throw new MemberServiceException("Authentication failed", e);
        }
    }

    @Override
    public MemberResponseDto updateProfile(Long id, MemberUpdateDto dto) {
        try {
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException(id));
            
            modelMapper.map(dto, member);
            Member updated = memberRepository.save(member);
            return modelMapper.map(updated, MemberResponseDto.class);
        } catch (Exception e) {
            if (e instanceof MemberNotFoundException) {
                throw e;
            }
            throw new MemberServiceException("Failed to update member profile", e);
        }
    }

    @Override
    public MemberResponseDto getMemberById(Long id) {
        try {
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException(id));
            return modelMapper.map(member, MemberResponseDto.class);
        } catch (Exception e) {
            if (e instanceof MemberNotFoundException) {
                throw e;
            }
            throw new MemberServiceException("Failed to retrieve member", e);
        }
    }

    @Override
    public void deleteMember(Long id) {
        try {
            if (!memberRepository.existsById(id)) {
                throw new MemberNotFoundException(id);
            }
            memberRepository.deleteById(id);
        } catch (Exception e) {
            if (e instanceof MemberNotFoundException) {
                throw e;
            }
            throw new MemberServiceException("Failed to delete member", e);
        }
    }

    @Override
    public java.util.List<MemberResponseDto> getAllMembers() {
        try {
            return memberRepository.findAll().stream()
                    .map(member -> modelMapper.map(member, MemberResponseDto.class))
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            throw new MemberServiceException("Failed to retrieve members", e);
        }
    }
}
