package com.cts.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {
    private String name;
    private String phone;
    private String address;
    private String membershipStatus;
}
