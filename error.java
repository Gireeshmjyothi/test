public String uploadFile(MultipartFile file, String filePath) {
    logger.info("Uploading file into sftp.");
    try (InputStream inputStream = file.getInputStream()) {

        createDirectoriesIfNotExist(filePath); // 🔧 Ensures the path exists

        sftpChannel.cd(filePath);

        String originalFilename = file.getOriginalFilename();
        String newFilename = appendDateToFilename(originalFilename);

        sftpChannel.put(inputStream, newFilename);
        logger.info("File uploaded successfully: {}", newFilename);
        return "File uploaded as: " + newFilename;

    } catch (Exception e) {
        throw new BaseException(ErrorConstants.GENERIC_ERROR_CODE, e.getMessage());
    }
}
private void createDirectoriesIfNotExist(String path) {
    try {
        String[] folders = path.split("/");
        StringBuilder currentPath = new StringBuilder();
        sftpChannel.cd("/"); // Start from root

        for (String folder : folders) {
            if (folder == null || folder.trim().isEmpty()) continue;
            currentPath.append("/").append(folder);
            try {
                sftpChannel.cd(currentPath.toString());
            } catch (SftpException e) {
                // Folder doesn't exist — create and cd into it
                sftpChannel.mkdir(currentPath.toString());
                sftpChannel.cd(currentPath.toString());
            }
        }
    } catch (Exception e) {
        throw new BaseException(ErrorConstants.GENERIC_ERROR_CODE,
                "Failed to create remote directory path: " + path + " - " + e.getMessage());
    }
}
