package com.example.config;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Lazy;

@Configuration
public class SftpConfig {

    @Value("${sftp.host:}")
    private String host;

    @Value("${sftp.port:22}")
    private int port;

    @Value("${sftp.username:}")
    private String username;

    @Value("${sftp.password:}")
    private String password;

    @Value("${sftp.remote.directory:/}")
    private String remoteDirectory;

    @Lazy
    @Bean
    public Session getSession() {
        if (host.isEmpty() || username.isEmpty()) {
            System.err.println("SFTP config missing. Skipping session creation.");
            return null;
        }
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return session;
        } catch (Exception e) {
            System.err.println("SFTP session connection failed: " + e.getMessage());
            return null;
        }
    }

    @Lazy
    @Bean
    public ChannelSftp getChannelSftp(@Lazy Session session) {
        if (session == null) {
            System.err.println("SFTP session is not available. Skipping SFTP channel setup.");
            return null;
        }
        try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.cd(remoteDirectory);
            return sftpChannel;
        } catch (Exception e) {
            System.err.println("Failed to create SFTP channel: " + e.getMessage());
            return null;
        }
    }

    public String getRootPath() {
        return remoteDirectory;
    }
}



package com.example.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final ObjectProvider<ChannelSftp> sftpProvider;

    public String uploadFile(MultipartFile file, String filePath) {
        log.info("Uploading file into sftp.");
        ChannelSftp sftpChannel = sftpProvider.getIfAvailable();

        if (sftpChannel == null) {
            throw new IllegalStateException("SFTP is not configured or connected.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            createDirectoriesIfNotExist(sftpChannel, filePath);

            sftpChannel.cd(filePath);

            String originalFilename = file.getOriginalFilename();
            String newFilename = appendDateToFilename(originalFilename);

            sftpChannel.put(inputStream, newFilename);
            log.info("File uploaded successfully: {}", newFilename);
            return "File uploaded as: " + newFilename;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    private void createDirectoriesIfNotExist(ChannelSftp sftpChannel, String path) {
        try {
            String[] folders = path.split("/");
            StringBuilder currentPath = new StringBuilder();
            sftpChannel.cd("/");

            for (String folder : folders) {
                if (folder == null || folder.trim().isEmpty()) continue;
                currentPath.append("/").append(folder);
                try {
                    sftpChannel.cd(currentPath.toString());
                } catch (SftpException e) {
                    sftpChannel.mkdir(currentPath.toString());
                    sftpChannel.cd(currentPath.toString());
                    log.info("Created directory: {}", currentPath);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create remote directory path: " + path + " - " + e.getMessage(), e);
        }
    }

    private String appendDateToFilename(String originalFilename) {
        log.info("Appending date to given file");
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
