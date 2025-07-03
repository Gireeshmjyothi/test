@Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.remote.directory}")
    private String remoteDirectory;

    @Bean
    public Session getSession() throws JSchException{
        JSch jsch = new JSch();
        Session session = null;
        session = jsch.getSession(username, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        return session;
    }

    @Bean
    public ChannelSftp getChannelSftp(Session session) throws JSchException, SftpException {
        ChannelSftp sftpChannel = null;
        Channel channel = session.openChannel("sftp");
        channel.connect();
        sftpChannel = (ChannelSftp) channel;
        sftpChannel.cd(remoteDirectory);
        return sftpChannel;
    }

    public String getRootPath() {
       return remoteDirectory;
    }
