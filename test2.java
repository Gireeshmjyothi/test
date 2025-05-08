package com.epay.rns.service;

import com.epay.rns.entity.FileInfo;
import com.epay.rns.externalservice.SftpClientHelper;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.sbi.epay.logging.utility.LoggerFactoryUtility;
import com.sbi.epay.logging.utility.LoggerUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.epay.rns.util.RnSConstants.DATE_FORMAT;
import static com.epay.rns.util.RnSConstants.SFTP_FILE_SEPARATOR;

@Service
@RequiredArgsConstructor
public class SftpClientService {
    //TODO: Will download in temp unique file and store in DB.
    private static final String LOCAL_DOWNLOAD_DIR = "C:/SFTP/Client/Download/";
    //TODO: This will come from DAO layer(Cache/Admin Service).
    private static final String PROCESSED_DIR = "/processed/";
    private static final String ACKNOWLEDGEMENT_DIR = "/acknowledgment/";
    private static final List<String> SUBFOLDERS = Arrays.asList("/RnS/SBI", "/RnS/HDFC", "/RnS/BOB");

    private final LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private final SftpClientHelper sftpClient;

    public void findListOfFiles() {
        int processedCount = 0;
        try {
            sftpClient.connect();

            List<FileInfo> recentFiles = sftpClient.findListOfFiles(SUBFOLDERS);

            if (recentFiles.isEmpty()) {
                log.info("No files found.");
                return;
            }
            for (FileInfo fileInfo : recentFiles) {
                logFileInfo(fileInfo, new SimpleDateFormat(DATE_FORMAT));

                boolean success = processSingleFile(sftpClient, LOCAL_DOWNLOAD_DIR, fileInfo);
                if (success) {
                    processedCount++;
                }

                try {
                    sftpClient.createAcknowledgmentFile(fileInfo.getFileName(), success, ACKNOWLEDGEMENT_DIR);
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

    private boolean processSingleFile(SftpClientHelper sftpClient, String fileDownloadDir, FileInfo fileInfo) {
        try {
            sftpClient.downloadFile(fileInfo.getRemotePath(), fileDownloadDir);
            log.info("Downloaded to: {}", fileDownloadDir);

            String processedPath = PROCESSED_DIR + SFTP_FILE_SEPARATOR + fileInfo.getFileName();
            sftpClient.moveFile(fileInfo.getRemotePath(), processedPath);
            log.info("File moved to: {}", processedPath);
            return true;

        } catch (SftpException | JSchException e) {
            log.error("Error while processing file {}", e.getMessage());
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
