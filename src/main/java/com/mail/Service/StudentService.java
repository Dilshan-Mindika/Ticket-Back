package com.mail.Service;

import com.mail.Model.Student;
import com.mail.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student findById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student validateById(String id) {
        return studentRepository.findActiveStudentById(id);
    }

    public boolean deleteById(String id) {
        return studentRepository.findById(id).map(student -> {
            student.setStatus("inactive");
            studentRepository.save(student);
            return true;
        }).orElse(false);
    }

    public List<Student> getAllActiveStudents() {
        return studentRepository.findByStatus("active");
    }
}
