public void createAcknowledgmentFile(String fileName, boolean success, String filePath) throws SftpException {
        String status = success ? "processed" : "failed";
        String ackPath = filePath + SFTP_FILE_SEPARATOR + fileName + "_" + status + ".txt";
        try {
            // Create an empty file by uploading a temporary empty file
            Path tempFile = Files.createTempFile("ack-", ".tmp");
            channelSftp.put(tempFile.toString(), ackPath);
            Files.delete(tempFile); // Clean up temporary file
            log.info("Created acknowledgment file : {}", ackPath);
        } catch (IOException | SftpException e) {
            throw new SftpException(ChannelSftp.SSH_FX_FAILURE, "Failed to create acknowledgment file " + ackPath + ": " + e.getMessage());
        }
    }
