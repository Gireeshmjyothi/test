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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SftpClientService {
    //TODO: Will download in temp unique file and store in DB.
    private static final String LOCAL_DOWNLOAD_DIR = "C:\\SFTP\\DownLoad";
    private static final String PROCESSED_DIR = "/processed"; // Server's processed folder
    private static final String ACKNOWLEDGEMENT_DIR = "/Acknowledgement"; // create Acknowledgement processed folder
    private final LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private final SFTPConfig sftpConfig;

    public void findListOfFiles() throws SftpException {

        SftpClientUtil sftpClient = sftpConfig.sftpClient();
        Path downloadDir = Paths.get(LOCAL_DOWNLOAD_DIR);
        try {

            // Connect to the SFTP server
            sftpClient.connect();

            //TODO: This will come from DAO layer(Cache/Admin Service).
            List<String> subfolders = Arrays.asList("/RnS/SBI", "/RnS/HDFC", "/RnS/BOB");

            List<FileInfo> recentFiles = sftpClient.findListOfFiles(subfolders);
            if (recentFiles.isEmpty()) {
                log.info("No files found found.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int processedCount = 0;
            for (FileInfo fileInfo : recentFiles) {

                log.info("Folder : {}", fileInfo.getFolder());
                log.info("File : {}" , fileInfo.getFileName());
                log.info("Path : {}" , fileInfo.getRemotePath());
                log.info("Modified : {}" , sdf.format(new Date(fileInfo.getModificationTime())));
                boolean noneMatch = subfolders.stream().noneMatch(value -> value.contains(fileInfo.getFileName()));

                boolean success = false;
                try {
                    // Download the file
                    Path downloadedFile = downloadDir.resolve(fileInfo.getFileName());
                    String downloadedFilePath = downloadedFile.toString();
                    sftpClient.downloadFile(fileInfo.getRemotePath(), downloadedFilePath);
                    log.info("Downloaded to : {}", downloadedFile);

                    // Verify content
                    String downloadedContent = Files.readString(downloadedFile);
                    log.info("Content : {}", downloadedContent);

                    // Move the file to /processed
                    String processedPath = PROCESSED_DIR + "/" + fileInfo.getFileName();
                    sftpClient.moveFile(fileInfo.getRemotePath(), processedPath);
                    log.info("File Moved to : {}", processedPath);
                    success = true;
                    processedCount++;
                } catch (SftpException | IOException e) {
                    log.error("Error processing file : {}", e.getMessage());
                }

                // Create acknowledgment file
                try {
                    sftpClient.createAcknowledgmentFile(fileInfo.getFileName(), success);
                } catch (SftpException e) {
                    log.error("Error creating acknowledgment file : {}" + e.getMessage());
                }

            }
            log.info("Total files downloaded: {}", recentFiles.size());

        } catch (JSchException | SftpException e) {
            log.error("Error occurred while process file to SFTP : {}", e.getMessage());
        } finally {
            sftpClient.disconnect();
            log.error("Disconnected from SFTP server");
        }
    }

}

