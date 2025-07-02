public static void main(String[] args) {
        final String rootDir = "C:/SFTP/Server";
        final int port = 2222;
        final String userName = "root";
        final String password = "root";
        try {
            SshServer sshd = SshServer.setUpDefaultServer();
            sshd.setPort(port);
            sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser")));
            sshd.setPasswordAuthenticator((u, p, session) -> u.equals(userName) && p.equals(password));
            sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
            sshd.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get(rootDir)));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    logger.info("Shutting down SFTP server...");
                    sshd.stop();
                } catch (IOException e) {
                    logger.error("Error while stopping SFTP server: " + e.getMessage());
                }
            }));

            sshd.start();
           logger.info("SFTP server started on port " + port + ", root: " + rootDir);
            Thread.currentThread().join();
        } catch (Exception e) {
           logger.error("Failed to start SFTP server: " + e.getMessage());
        }
    }
