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

