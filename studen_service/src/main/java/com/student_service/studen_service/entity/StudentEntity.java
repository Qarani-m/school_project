package com.student_service.studen_service.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.util.Date;

@Entity(name = "students")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
    private boolean isPresent;
    private  boolean isEnrolled;
    private String enrollmentDate;


}
