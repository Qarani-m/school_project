package com.student_service.studen_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String admNo;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;
    private String email;
    private String guardianName;
    private String guardianPhoneNumber;
    private String classId;
    private String level;
    private String stream;

    private boolean isPresent =true;
    private  boolean isEnrolled = true;
    private String enrollmentDate = LocalDate.now().toString();

}
