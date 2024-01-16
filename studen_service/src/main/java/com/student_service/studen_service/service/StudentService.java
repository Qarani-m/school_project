package com.student_service.studen_service.service;

import com.student_service.studen_service.dto.StudentDto;
import com.student_service.studen_service.entity.StudentEntity;

import java.io.FileInputStream;
import java.util.List;

public interface StudentService {
    List<StudentEntity> getAllStudents();

    List<StudentEntity> getAllStudentsEnrolled();

    StudentEntity getStudentByAdmNo(String studentId);

    StudentEntity getStudentById(Long studentId);

    String addStudent(StudentDto student);

    String addStudentList(FileInputStream filePath);

    String updateStudent(String studentId, StudentDto updatedStudent);

    String deleteStudent(String studentId);

    List<StudentEntity> getStudentsByLevel(String level);

    List<StudentEntity> getStudentsByStream(String stream);
}
