package com.mail.Repository;

import com.mail.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findByStudentId(String studentId);
    List<Student> findByStatus(String status);

    @Query("SELECT s FROM Student s WHERE s.status = 'active' AND s.id = ?1")
    Student findActiveStudentById(String id);
}
