package com.mail.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String studentId;
    private String name;
    private String batch;
    private String phoneNumber;
    private String email;

    private String status = "active";

    @Lob
    private String qrCodeBase64;

    private LocalDate qrCodeGeneratedDate;
    private LocalTime qrCodeGeneratedTime;

    // Default constructor
    public Student() {
    }

    // Custom constructor to set all the required fields
    public Student(String studentId, String name, String batch, String phoneNumber, String email, String status, String qrCodeBase64, LocalDate qrCodeGeneratedDate, LocalTime qrCodeGeneratedTime) {
        this.studentId = studentId;
        this.name = name;
        this.batch = batch;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = status;
        this.qrCodeBase64 = qrCodeBase64;
        this.qrCodeGeneratedDate = qrCodeGeneratedDate;
        this.qrCodeGeneratedTime = qrCodeGeneratedTime;
    }

    // Getter and Setter methods for all fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrCodeBase64() {
        return qrCodeBase64;
    }

    public void setQrCodeBase64(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }

    public LocalDate getQrCodeGeneratedDate() {
        return qrCodeGeneratedDate;
    }

    public void setQrCodeGeneratedDate(LocalDate qrCodeGeneratedDate) {
        this.qrCodeGeneratedDate = qrCodeGeneratedDate;
    }

    public LocalTime getQrCodeGeneratedTime() {
        return qrCodeGeneratedTime;
    }

    public void setQrCodeGeneratedTime(LocalTime qrCodeGeneratedTime) {
        this.qrCodeGeneratedTime = qrCodeGeneratedTime;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", batch='" + batch + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", qrCodeBase64='" + qrCodeBase64 + '\'' +
                ", qrCodeGeneratedDate=" + qrCodeGeneratedDate +
                ", qrCodeGeneratedTime=" + qrCodeGeneratedTime +
                '}';
    }
}
