@Configuration
public class SftpConfig {

    @Value("${sftp.host:}")
    private String host;

    @Value("${sftp.port:22}")
    private int port;

    @Value("${sftp.username:}")
    private String username;

    @Value("${sftp.password:}")
    private String password;

    @Value("${sftp.remote.directory:/}")
    private String remoteDirectory;

    @Lazy
    @Bean
    public Session getSession() {
        if (host.isEmpty() || username.isEmpty()) {
            System.err.println("SFTP config missing. Skipping session creation.");
            return null;
        }
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return session;
        } catch (Exception e) {
            System.err.println("SFTP session connection failed: " + e.getMessage());
            return null;
        }
    }

    @Lazy
    @Bean
    public ChannelSftp getChannelSftp(@Autowired(required = false) Session session) {
        if (session == null) {
            System.err.println("SFTP session is not available. Skipping SFTP channel setup.");
            return null;
        }
        try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.cd(remoteDirectory); // optional
            return sftpChannel;
        } catch (Exception e) {
            System.err.println("Failed to create SFTP channel: " + e.getMessage());
            return null;
        }
    }

    public String getRootPath() {
        return remoteDirectory;
    }
}

@RequiredArgsConstructor
@Service
public class FileUploadService {

    private final ChannelSftp channelSftp;

    public void uploadFile(...) {
        if (channelSftp == null) {
            throw new IllegalStateException("SFTP is not configured or connected.");
        }
        // Proceed with upload
    }
}


public String uploadFile(MultipartFile file, String filePath) {
        logger.info("Uploading file into sftp.");
        try (InputStream inputStream = file.getInputStream()) {
            if (sftpChannel == null) {
                throw new BaseException(ErrorConstants.GENERIC_ERROR_CODE, "SFTP is not configured or connected.");
            }
            createDirectoriesIfNotExist(filePath);

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
                    sftpChannel.mkdir(currentPath.toString());
                    sftpChannel.cd(currentPath.toString());
                }
            }
        } catch (Exception e) {
            throw new BaseException(ErrorConstants.GENERIC_ERROR_CODE,
                    "Failed to create remote directory path: " + path + " - " + e.getMessage());
        }
    }

    private String appendDateToFilename(String originalFilename) {
        logger.info("Appending date to given file");
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            String namePart = originalFilename.substring(0, dotIndex);
            String extension = originalFilename.substring(dotIndex);
            return namePart + "_" + date + extension;
        } else {
            return originalFilename + "_" + date;
        }
    }

