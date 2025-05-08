private final LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private final String host;
    private final String username;
    private final String password;
    private final int port;
    private Session session;
    private ChannelSftp channelSftp;

    public void connect() throws JSchException {
        log.info("Preparing SFTP Connection.");
        JSch jsch = new JSch();
        session = jsch.getSession(username, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no"); // For testing purposes
        session.connect();
        channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        log.info("Connected to SFTP server : {}", host);
    }

    public void disconnect() {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        log.info("Disconnected from SFTP server.");
    }

public void downloadFile(String remoteFilePath, String localFilePath) throws JSchException, SftpException {
        channelSftp.get(remoteFilePath, localFilePath);
    }


    public void moveFile(String sourcePath, String destinationPath) throws SftpException {
        try {
            channelSftp.rename(sourcePath, destinationPath);
        } catch (SftpException e) {
            throw new SftpException(e.id, "Failed to move file from " + sourcePath + " to " + destinationPath + ": " + e.getMessage());
        }
    }


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

    public List<FileInfo> findListOfFiles(List<String> remoteSubfolders) throws SftpException {
        List<FileInfo> recentFiles = new ArrayList<>();

        for (String subfolder : remoteSubfolders) {
            try {
                Vector<ChannelSftp.LsEntry> files = channelSftp.ls(subfolder);
                for (ChannelSftp.LsEntry entry : files) {
                    if (!entry.getAttrs().isDir() && !entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                        long mTime = entry.getAttrs().getMTime() * 1000L; // Convert to milliseconds
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFolder(subfolder);
                        fileInfo.setFileName(entry.getFilename());
                        fileInfo.setRemotePath(subfolder + SFTP_FILE_SEPARATOR + entry.getFilename());
                        fileInfo.setModificationTime(mTime);
                        recentFiles.add(fileInfo);
                    }
                }
            } catch (SftpException e) {
                log.error("Error while listing files : {}", e.getMessage());
            }
        }
        return recentFiles;
    }
