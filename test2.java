public void moveFile(String sourcePath, String destinationPath) throws SftpException {
        try {
            channelSftp.rename(sourcePath, destinationPath);
        } catch (SftpException e) {
            throw new SftpException(e.id, "Failed to move file from " + sourcePath + " to " + destinationPath + ": " + e.getMessage());
        }
    }
