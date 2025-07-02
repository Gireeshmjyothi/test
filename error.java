package com.example.sftpupload.service;

import com.jcraft.jsch.ChannelSftp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SftpUploadService {

    private final ChannelSftp channelSftp;

    @Value("${sftp.remote-dir}")
    private String remoteDir;

    public String uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            channelSftp.cd(remoteDir);

            // 🔽 Append current date to filename (e.g., report_2025-07-02.csv)
            String originalFilename = file.getOriginalFilename();
            String newFilename = appendDateToFilename(originalFilename);

            channelSftp.put(inputStream, newFilename);

            return "File uploaded as: " + newFilename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    private String appendDateToFilename(String originalFilename) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            String namePart = originalFilename.substring(0, dotIndex);
            String extension = originalFilename.substring(dotIndex);
            return namePart + "_" + date + extension;
        } else {
            return originalFilename + "_" + date;
        }
    }
}
