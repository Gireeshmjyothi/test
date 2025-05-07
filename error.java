package com.epay.rns.service;

import com.epay.rns.config.SFTPConfig;
import com.epay.rns.entity.FileInfo;
import com.epay.rns.externalservice.SftpClientUtil;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.sbi.epay.logging.utility.LoggerFactoryUtility;
import com.sbi.epay.logging.utility.LoggerUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SftpClientService {

    private static final String LOCAL_DOWNLOAD_DIR = "C:\\SFTP\\DownLoad";
    private static final String PROCESSED_DIR = "/processed";
    private static final List<String> SUBFOLDERS = Arrays.asList("/RnS/SBI", "/RnS/HDFC", "/RnS/BOB");

    private final LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private final SFTPConfig sftpConfig;

    public void findListOfFiles() throws SftpException {
        SftpClientUtil sftpClient = sftpConfig.sftpClient();
        Path downloadDir = Paths.get(LOCAL_DOWNLOAD_DIR);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            sftpClient.connect();
            List<FileInfo> recentFiles = sftpClient.findListOfFiles(SUBFOLDERS);

            if (recentFiles.isEmpty()) {
                log.info("No files found.");
                return;
            }

            int processedCount = 0;

            for (FileInfo fileInfo : recentFiles) {
                logFileInfo(fileInfo, sdf);

                boolean success = processSingleFile(sftpClient, downloadDir, fileInfo);
                if (success) {
                    processedCount++;
                }

                try {
                    sftpClient.createAcknowledgmentFile(fileInfo.getFileName(), success);
                } catch (SftpException e) {
                    log.error("Error creating acknowledgment file: {}", e.getMessage());
                }
            }

            log.info("Total files downloaded and processed: {}", processedCount);

        } catch (JSchException | SftpException e) {
            log.error("Error occurred while processing files on SFTP: {}", e.getMessage());
        } finally {
            sftpClient.disconnect();
            log.info("Disconnected from SFTP server.");
        }
    }

    private boolean processSingleFile(SftpClientUtil sftpClient, Path downloadDir, FileInfo fileInfo) {
        Path downloadedFile = downloadDir.resolve(fileInfo.getFileName());

        try {
            sftpClient.downloadFile(fileInfo.getRemotePath(), downloadedFile.toString());
            log.info("Downloaded to: {}", downloadedFile);

            String content = new String(Files.readAllBytes(downloadedFile));
            log.info("Content: {}", content);

            String processedPath = PROCESSED_DIR + "/" + fileInfo.getFileName();
            sftpClient.moveFile(fileInfo.getRemotePath(), processedPath);
            log.info("File moved to: {}", processedPath);

            return true;

        } catch (IOException | SftpException e) {
            log.error("Error processing file {}: {}", fileInfo.getFileName(), e.getMessage());
            return false;
        }
    }

    private void logFileInfo(FileInfo fileInfo, SimpleDateFormat sdf) {
        log.info("Folder: {}", fileInfo.getFolder());
        log.info("File: {}", fileInfo.getFileName());
        log.info("Path: {}", fileInfo.getRemotePath());
        log.info("Modified: {}", sdf.format(new Date(fileInfo.getModificationTime())));
    }
}
