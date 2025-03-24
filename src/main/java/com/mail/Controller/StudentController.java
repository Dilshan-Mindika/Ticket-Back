package com.mail.Controller;

import com.mail.Model.Student;
import com.mail.Repository.StudentRepository;
import com.mail.Service.QrCodeService;
import com.mail.Service.StudentService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Base64;

@CrossOrigin(origins = "http://localhost:6005/")
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/generate-qrcode")
    public ResponseEntity<String> generateQrCode(@RequestBody Student student) {
        validateStudent(student);

        // Create a new student object to persist using the custom constructor
        Student studentData = new Student(
                student.getStudentId(),
                student.getName(),
                student.getBatch(),
                student.getPhoneNumber(),
                student.getEmail(),
                "active",  // Default status is "active"
                null,  // Initially no QR code
                null,  // Initially no date for QR code
                null   // Initially no time for QR code
        );

        // Save the student to generate the ID
        studentRepository.save(studentData);

        // Now that the student has an ID, we can safely use it to generate the QR code
        String qrCodeBase64 = qrCodeService.generateQRCode(studentData);

        // Update the student data with the generated QR code and other details
        studentData.setQrCodeBase64(qrCodeBase64);
        studentData.setQrCodeGeneratedDate(LocalDate.now());
        studentData.setQrCodeGeneratedTime(LocalTime.now());

        // Save again with QR code details
        studentRepository.save(studentData);

        // Send the email with the QR code
        sendEmail(studentData.getEmail(), qrCodeBase64, studentData.getName(), studentData.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("QR Code sent to " + studentData.getEmail());
    }

    private void validateStudent(Student student) {
        if (student.getName() == null || student.getEmail() == null) {
            throw new IllegalArgumentException("Name and email must not be null");
        }
    }

    private void sendEmail(String to, String qrCodeBase64, String studentName, String studentId) {
        try {
            // Create a new MimeMessage for the email
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("KIU Unplugged with ELIX - The Music Concert Ticket for " + studentName);

            // Generate the HTML content for the email
            String htmlContent = createHtmlContent(studentName, studentId);
            helper.setText(htmlContent, true);

            // Decode the QR code from Base64 and add it as an inline image
            byte[] imageBytes = Base64.getDecoder().decode(qrCodeBase64);
            ByteArrayResource resource = new ByteArrayResource(imageBytes);
            helper.addInline("qr_code", resource, "image/png");

            // Send the email
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createHtmlContent(String name, String studentId) {
        // Generate the ticket number dynamically based on studentId
        String ticketNumber = "EU-" + studentId.toString().substring(0,15).toUpperCase();

        // Return the HTML content for the email
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>KIU Unplugged with ELIX - The Music Concert Ticket</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: transparent; color: #fff; text-align: center; padding: 20px; margin: 0; position: relative; }" +
                "h1 { color: #000; font-size: 2.5em; margin-bottom: 15px; font-weight: bold; }" +
                ".ticket { border: 3px solid #000; padding: 20px; margin: 20px auto; border-radius: 10px; background-color: rgba(0, 0, 0, 0.7); color: #fff; width: 90%; max-width: 600px; position: relative; }" +
                ".qr-code { margin-top: 15px; max-width: 100%; height: auto; }" +
                ".highlight { color: #ffcc00; font-weight: bold; }" +
                ".footer { margin-top: 20px; font-size: 0.9em; color: #888; }" +
                ".contact { margin-top: 30px; padding: 5px; border-radius: 14px; font-size: 0.9em; }" +
                ".contact a { color: #ffcc00; text-decoration: none; }" +
                ".contact a:hover { color: #fff; }" +
                ".logos { margin-top: 30px; display: flex; justify-content: center; align-items: center; position: relative; }" +
                ".logo { width: 200px; height: auto; }" +
                ".ticket-number { position: absolute; top: 20px; right: 20px; color: #ffcc00; font-size: 14px; font-weight: bold; writing-mode: vertical-rl; text-align: center; white-space: nowrap; z-index: 10; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"logos\">" +
                "<img src=\"https://i.imghippo.com/files/PXy9590Bo.png\" alt=\"KIU Unplugged Logo\" style=\"max-width:  300px; margin-bottom: 10px;\">" +
                "<div class=\"ticket-number\">" + ticketNumber + "</div>" +
                "</div>" +
                "<div class=\"ticket\">" +
                "<h2>🎫 Event Ticket</h2>" +
                "<p><strong>Hello, " + name + "!</strong></p>" +
                "<p>Congratulations!<br>You have successfully registered for the <br><br><span class=\"highlight\">KIU Unplugged with ELIX - The Music Concert</span>.</p>" +
                "<p>This is your official e-ticket for the event. Please present this ticket at the entrance to gain access to the concert.</p>" +
                "<h3><span class=\"highlight\"><br>Event Details:</span></h3>" +
                "<p><strong>📅 Date:</strong> 3rd April 2025</p>" +
                "<p><strong>⏰ Time:</strong> 18:00 - 21:00</p>" +
                "<p><strong>📍 Venue:</strong> KIU Auditorium</p>" +
                "<p><br><strong>⚠️ Please note:</strong> Make sure to arrive at least 30 minutes before the event to secure a good spot!</p>" +
                "<p><span class=\"highlight\">Mark Your Calendars! A Concert You Can’t Miss!</span></p>" +
                "<p>To access the event, scan the QR code below at the entrance:</p>" +
                "<img src=\"cid:qr_code\" class=\"qr-code\" alt=\"QR Code\">" +
                "<div class=\"contact\">" +
                "<h3>Need Assistance? Contact Us!</h3>" +
                "<p>If you have any issues or need help with your ticket, feel free to reach out:</p>" +
                "<p><strong>📞 Call us at<br></strong><a href=\"tel:+94767545720\">076 754 5720 (Pansilu Somarathne)</a> <br> <a href=\"tel:+94768858646\">076 885 8646 (Yuvindu WickramaArachchi)</a></p>" +
                "</div>" +
                "</div>" +
                "<div class=\"logos\">" +
                "<div class=\"logo-container\">" +
                "<img src=\"https://i.imghippo.com/files/vLkJ8823gGI.png\" alt=\"Bottom logo\" class=\"logo\">" +
                "</div>" +
                "</div>" +
                "<p class=\"footer\">© 2025 KIU Unplugged with ELIX- The Music Concert | All Rights Reserved by <a href=\"https://dilshanmindika.pro/\" style=\"color: #ffcc00; text-decoration: none;\">DILA</a></p>" +
                "</body>" +
                "</html>";
    }
}
