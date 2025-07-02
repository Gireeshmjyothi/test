package com.example.sftpupload.service;

import com.jcraft.jsch.ChannelSftp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class SftpUploadService {

    private final ChannelSftp channelSftp;

    @Value("${sftp.remote-dir}")
    private String remoteDir;

    public String uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            channelSftp.cd(remoteDir);
            channelSftp.put(inputStream, file.getOriginalFilename());
            return "File uploaded successfully to SFTP";
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }
}
