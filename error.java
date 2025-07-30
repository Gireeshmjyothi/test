import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sftp")
@RequiredArgsConstructor
public class SFTPDownloadController {

    private final SFTPService sftpService;

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String filePath, @RequestParam String fileName) {
        try {
            byte[] fileData = sftpService.downloadFile(filePath, fileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileData);

        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(404)
                    .body("Error downloading file: " + ex.getMessage());
        }
    }
} u


import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Slf4j
@Service
public class SFTPService {

    @Value("${sftp.host}")
    private String sftpHost;

    @Value("${sftp.port}")
    private int sftpPort;

    @Value("${sftp.username}")
    private String sftpUsername;

    @Value("${sftp.password}")
    private String sftpPassword;

    public byte[] downloadFile(String filePath, String fileName) {
        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpUsername, sftpHost, sftpPort);
            session.setPassword(sftpPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;

            String fullPath = filePath.endsWith("/") ? filePath + fileName : filePath + "/" + fileName;
            log.info("Downloading file from SFTP: {}", fullPath);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            sftpChannel.get(fullPath, outputStream);

            return outputStream.toByteArray();

        } catch (SftpException e) {
            throw new RuntimeException("File not found on SFTP server: " + e.getMessage(), e);
        } catch (JSchException e) {
            throw new RuntimeException("SFTP connection error: " + e.getMessage(), e);
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) sftpChannel.disconnect();
            if (session != null && session.isConnected()) session.disconnect();
        }
    }
}
    
