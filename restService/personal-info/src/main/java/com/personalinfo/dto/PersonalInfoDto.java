package com.personalinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
  Data transfer object for Personal Info
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfoDto {

    private String firstName;
    private String lastName;
    private String dateOfBirth;    // in MMDDYYYY format
    private String email;
    private String phoneNumber;   // in XXX-XXX-XXXX format

}
