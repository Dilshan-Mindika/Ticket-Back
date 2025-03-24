package com.mail.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mail.Model.Student;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;

@Service
public class QrCodeService {

    public String generateQRCode(Student student) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        String uuidData = student.getId().toString(); // QR contains only UUID

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(uuidData, BarcodeFormat.QR_CODE, 400, 400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
            byte[] qrCodeBytes = baos.toByteArray();

            String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);

            student.setQrCodeBase64(uuidData);
            student.setQrCodeGeneratedDate(currentDate);
            student.setQrCodeGeneratedTime(currentTime);

            return qrCodeBase64;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
