package com.student_service.studen_service.controller;


import com.student_service.studen_service.dto.StudentDto;
import com.student_service.studen_service.entity.StudentEntity;
import com.student_service.studen_service.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @GetMapping("/all")
    public ResponseEntity<List<StudentEntity>> getAllStudents() {
        // Get all students
        List<StudentEntity> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/all-enrolled")
    public ResponseEntity<List<StudentEntity>> getAllStudentsEnrolled() {
        // Get all students
        List<StudentEntity> students = studentService.getAllStudentsEnrolled();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/one/{studentId}")
    public ResponseEntity<StudentEntity> getStudentById(@PathVariable Long studentId) {
        // Get a specific student by ID
        StudentEntity student = studentService.getStudentById(studentId);
        return student != null ?
                new ResponseEntity<>(student, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<String> addStudent(@RequestBody StudentDto studentDto) {
        // Add a new student
       String message=  studentService.addStudent(studentDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/create-list")
    public ResponseEntity<Void> addStudentList(@RequestBody FileInputStream filePath) {
        // Add a new student
        studentService.addStudentList(filePath);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PutMapping("/update/{studentId}")
    public ResponseEntity<Void> updateStudent(@PathVariable String studentId, @RequestBody StudentDto updatedStudent) {
        // Update an existing student
        String isUpdated = studentService.updateStudent(studentId, updatedStudent);
        return isUpdated.contains("No Student found") ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND):
                new ResponseEntity<>(HttpStatus.OK) ;
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        // Delete a student by ID
        String isDeleted = studentService.deleteStudent(studentId);
        return isDeleted.contains("Deleted") ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/level/{level}")
    public ResponseEntity<List<StudentEntity>> getStudentsByLevel(@PathVariable String level) {
        // Get all students in a specific level
        List<StudentEntity> students = studentService.getStudentsByLevel(level);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/stream/{stream}")
    public ResponseEntity<List<StudentEntity>> getStudentsByStream(@PathVariable String stream) {
        // Get all students in a single stream
        List<StudentEntity> students = studentService.getStudentsByStream(stream);
        return new ResponseEntity<>(students, HttpStatus.OK);

    }

    @GetMapping("/adm/{adm}")
    public ResponseEntity<String> getStudentsByAdm(@PathVariable String adm) {
        ResponseEntity<String> response;
        StudentEntity student = studentService.getStudentByAdmNo(adm);

        if (student != null) {
            response = new ResponseEntity<>(student.toString(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        }

        return response;
    }
}