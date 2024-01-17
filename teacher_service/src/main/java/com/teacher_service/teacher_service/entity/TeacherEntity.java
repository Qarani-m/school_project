package com.teacher_service.teacher_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "teachers")
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String teacherId;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String qualification;
    private String[] coursesTaught;
    private String classTeacher;
    private boolean bomEmployee;

    private boolean isPresent;
}
