public String uploadFile(MultipartFile file, String filePath) {
        logger.info("Uploading file into sftp.");
        try (InputStream inputStream = file.getInputStream()) {

            sftpChannel.cd(filePath);

            String originalFilename = file.getOriginalFilename();
            String newFilename = appendDateToFilename(originalFilename);

            sftpChannel.put(inputStream, newFilename);
            logger.info("File uploaded successfully : {}", newFilename);
            return "File uploaded as: " + newFilename;
        } catch (Exception e) {
            throw new BaseException(ErrorConstants.GENERIC_ERROR_CODE, e.getMessage());
        }
    }
