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

            // Build remote path relative to root (mapped as C:/)
            String fullPath = baseRemoteDir + (subPath != null && !subPath.isBlank() ? "/" + subPath : "");

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

        for (String folder : folders) {
            if (folder == null || folder.trim().isEmpty()) continue;
            try {
                sftp.cd(folder);
            } catch (SftpException e) {
                sftp.mkdir(folder);
                sftp.cd(folder);
                log.info("Created directory: {}", folder);
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
