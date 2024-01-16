package com.student_service.studen_service.repository;

import com.student_service.studen_service.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    List<StudentEntity> findAllByIsEnrolledTrue();
    StudentEntity findByAdmNo(String admNo);
    StudentEntity existsByAdmNo(String admNo);
    List<StudentEntity> findByLevel(String level);
    List <StudentEntity> findByStream(String stream);

}
