package com.student_service.studen_service.service;


import com.student_service.studen_service.dto.StudentDto;
import com.student_service.studen_service.entity.StudentEntity;
import com.student_service.studen_service.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{


    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<StudentEntity> getAllStudents() {
        LocalDate fourYearsAgo = LocalDate.now().minusYears(4);
        return studentRepository.findAll().stream()
                .filter(student -> {
                    LocalDate enrollmentDateAsLocalDate = LocalDate.parse(student.getEnrollmentDate());
                    return enrollmentDateAsLocalDate.isAfter(fourYearsAgo);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentEntity> getAllStudentsEnrolled() {
        return studentRepository.findAllByIsEnrolledTrue();
    }

    @Override
    public StudentEntity getStudentByAdmNo(String studentId) {
        return studentRepository.findByAdmNo(studentId);
    }

    @Override
    public StudentEntity getStudentById(Long studentId) {
        return studentRepository.findById(studentId).get();
    }

    @Override
    public String addStudent(StudentDto studentDto) {
        // Convert StudentDto to StudentEntity using the builder pattern
        StudentEntity newStudent = StudentEntity.builder()
                .admNo(studentDto.getAdmNo())
                .firstName(studentDto.getFirstName())
                .level(studentDto.getLevel())
                .stream(studentDto.getStream())
                .lastName(studentDto.getLastName())
                .dateOfBirth(studentDto.getDateOfBirth())
                .address(studentDto.getAddress())
                .phoneNumber(studentDto.getPhoneNumber())
                .email(studentDto.getEmail())
                .guardianName(studentDto.getGuardianName())
                .guardianPhoneNumber(studentDto.getGuardianPhoneNumber())
                .classId(studentDto.getClassId())
                .isPresent(studentDto.isPresent())
                .isEnrolled(studentDto.isEnrolled())
                .enrollmentDate(studentDto.getEnrollmentDate())
                .build();
        studentRepository.save(newStudent);
        return "Student added successfully";
    }

    public String addStudentList(FileInputStream fileInputStream) {
        try {
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                StudentEntity newStudent =  StudentEntity.builder()
                        .admNo(getStringCellValue(row.getCell(0)))
                        .firstName(getStringCellValue(row.getCell(1)))
                        .lastName(getStringCellValue(row.getCell(2)))
                        .dateOfBirth(getStringCellValue(row.getCell(2)))
                        .address(  getStringCellValue(row.getCell(4))   )
                        .phoneNumber(getStringCellValue(row.getCell(5)))
                        .email(getStringCellValue(row.getCell(6)))
                        .guardianName(getStringCellValue(row.getCell(7)))
                        .guardianPhoneNumber(getStringCellValue(row.getCell(8)))
                        .classId(getStringCellValue(row.getCell(9)))
                        .level(getStringCellValue(row.getCell(10)))
                        .stream(getStringCellValue(row.getCell(11)))
                        .isPresent(true)
                        .isEnrolled(true)
                        .enrollmentDate(LocalDate.now().toString())
                        .build();
                studentRepository.save(newStudent);
            }
            return "Students added successfully from the Excel file.";
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
            return "Failed to add students. Error: " + e.getMessage();
        }
    }

    private String getStringCellValue(Cell cell) {
        return cell == null ? null : cell.getStringCellValue();
    }

    @Override
    public String updateStudent(String studentId, StudentDto updatedStudent) {
        StudentEntity existingStudent = studentRepository.findByAdmNo(studentId);
        if (existingStudent == null) {
            return "No Student found";
        }
        StudentEntity updatedEntity = StudentEntity.builder()
                .admNo(updatedStudent.getAdmNo())
                .firstName(updatedStudent.getFirstName())
                .lastName(updatedStudent.getLastName())
                .dateOfBirth(updatedStudent.getDateOfBirth())
                .address(updatedStudent.getAddress())
                .phoneNumber(updatedStudent.getPhoneNumber())
                .email(updatedStudent.getEmail())
                .guardianName(updatedStudent.getGuardianName())
                .guardianPhoneNumber(updatedStudent.getGuardianPhoneNumber())
                .classId(updatedStudent.getClassId())
                .isPresent(updatedStudent.isPresent())
                .isEnrolled(updatedStudent.isEnrolled())
                .enrollmentDate(updatedStudent.getEnrollmentDate())
                .level(updatedStudent.getLevel())
                .stream(updatedStudent.getStream())
                .build();
        updatedEntity.setId(existingStudent.getId());
        studentRepository.save(updatedEntity);
        return "Student updates succesfully";
    }
    @Override
    public String deleteStudent(String studentId) {
        StudentEntity student = studentRepository.findByAdmNo(studentId);
        if (student != null) {
            studentRepository.deleteById(student.getId());
            return "Student Not Found";
        }
        return "Student Deleted Succesfully";
    }
    @Override
    public List<StudentEntity> getStudentsByLevel(String level) {
        return studentRepository.findByLevel(level);
    }
    @Override
    public List<StudentEntity> getStudentsByStream(String stream) {
        return studentRepository.findByStream(stream);
    }
}
