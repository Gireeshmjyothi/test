public void uploadFile(String localFilePath, String remoteFilePath) throws JSchException, SftpException {
        File localFile = new File(localFilePath);
        if (!localFile.exists()) {
            throw new SftpException(ChannelSftp.SSH_FX_NO_SUCH_FILE, "Local file does not exist: " + localFilePath);
        }
        channelSftp.put(localFilePath, remoteFilePath);
    }
