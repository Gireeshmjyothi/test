public String uploadToSftp(MultipartFile file, String subPath) {
    ChannelSftp sftp = null;
    try (InputStream inputStream = file.getInputStream()) {
        sftp = SftpUtil.getSftpChannel(host, port, username, password);

        // 🧼 Clean both parts to avoid duplicate slashes or folders
        String cleanBase = baseRemoteDir.replaceAll("^/+", "").replaceAll("/+$", "");
        String cleanSub = (subPath != null ? subPath : "").replaceAll("^/+", "").replaceAll("/+$", "");

        String fullPath = cleanSub.isEmpty() ? cleanBase : cleanBase + "/" + cleanSub;

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
