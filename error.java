package com.example.util;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SftpUtil {

    public static ChannelSftp getSftpChannel(String host, int port, String username, String password) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            log.info("SFTP connection established.");
            return (ChannelSftp) channel;
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to SFTP: " + e.getMessage(), e);
        }
    }
}

package com.example.service;

import com.example.util.SftpUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class SftpUploadService {

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private Integer port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.remote.directory}")
    private String baseRemoteDir;

    public String uploadToSftp(MultipartFile file, String subPath) {
        ChannelSftp sftp = null;
        try (InputStream inputStream = file.getInputStream()) {
            sftp = SftpUtil.getSftpChannel(host, port, username, password);

            String fullPath = baseRemoteDir + (subPath != null ? "/" + subPath : "");
            createDirectoriesIfNotExist(sftp, fullPath);
            sftp.cd(fullPath);

            String newFileName = appendDateToFilename(file.getOriginalFilename());
            sftp.put(inputStream, newFileName);

            log.info("Uploaded to SFTP: {}/{}", fullPath, newFileName);
            return newFileName;

        } catch (Exception e) {
            throw new RuntimeException("SFTP upload failed: " + e.getMessage(), e);
        } finally {
            if (sftp != null) {
                try {
                    sftp.exit();
                    if (sftp.getSession() != null) sftp.getSession().disconnect();
                } catch (Exception ex) {
                    log.warn("Failed to disconnect SFTP session: {}", ex.getMessage());
                }
            }
        }
    }

    private void createDirectoriesIfNotExist(ChannelSftp sftp, String path) throws SftpException {
        String[] folders = path.split("/");
        StringBuilder currentPath = new StringBuilder();
        sftp.cd("/");

        for (String folder : folders) {
            if (folder == null || folder.trim().isEmpty()) continue;
            currentPath.append("/").append(folder);
            try {
                sftp.cd(currentPath.toString());
            } catch (SftpException e) {
                sftp.mkdir(currentPath.toString());
                sftp.cd(currentPath.toString());
                log.info("Created directory: {}", currentPath);
            }
        }
    }

    private String appendDateToFilename(String originalFilename) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int dotIndex = originalFilename.lastIndexOf('.');
        return (dotIndex > 0)
                ? originalFilename.substring(0, dotIndex) + "_" + date + originalFilename.substring(dotIndex)
                : originalFilename + "_" + date;
    }
}


package com.example.controller;

import com.example.service.SftpUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sftp")
@RequiredArgsConstructor
public class FileController {

    private final SftpUploadService sftpUploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file,
                                             @RequestParam(defaultValue = "") String path) {
        String uploadedFile = sftpUploadService.uploadToSftp(file, path);
        return ResponseEntity.ok("Uploaded: " + uploadedFile);
    }
}

                                        
