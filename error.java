 Algorithm negotiation fail: algorithmName="server_host_key" jschProposal="ssh-ed25519,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521,rsa-sha2-512,rsa-sha2-256" serverProposal="ssh-rsa"


// jsch
	implementation("com.github.mwiede:jsch:0.2.25")
this is used in another service (RandSService) to connecto sftp
and the blow deplendeces in other SFTP service which give me the localhost sftp server to connect
	// apache sshd for compile scope (RELEASE is not recommended in Gradle, use a fixed version instead)
	implementation 'org.apache.sshd:sshd-core:2.1.0'
	implementation 'org.apache.sshd:sshd-sftp:2.1.0'

	// https://mvnrepository.com/artifact/com.github.mwiede/jsch
	implementation("com.github.mwiede:jsch:0.2.25")

	// apache sshd for test scope
	testImplementation 'org.apache.sshd:sshd-core:2.1.0'
	testImplementation 'org.apache.sshd:sshd-sftp:2.1.0'


earlier it was working with 	// jsch
	implementation 'com.jcraft:jsch:0.1.54'

but now it is not working with updated version which is mentioned above



	/**
     * Setup a SFTP Server in localhost with a temp directory as root
     *
     * @throws IOException If it cannot create a temp dir
     */
    public static SftpServerBean setupSftpServer(String username, String password, int port) throws IOException {
        Path tempSftpDir = Paths.get( "C:\\SFTP") ;//Files.createTempDirectory(SFTPServer.class.getName());

        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<>();
        userAuthFactories.add(new UserAuthPasswordFactory());

        List<NamedFactory<Command>> sftpCommandFactory = new ArrayList<>();
        sftpCommandFactory.add(new SftpSubsystemFactory());

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshd.setUserAuthFactories(userAuthFactories);
        sshd.setCommandFactory(new ProcessShellCommandFactory());
        sshd.setSubsystemFactories(sftpCommandFactory);
        sshd.setPasswordAuthenticator((usernameAuth, passwordAuth, session) -> {
            if ((username.equals(usernameAuth)) && (password.equals(passwordAuth))) {
                sshd.setFileSystemFactory(new VirtualFileSystemFactory(tempSftpDir));
                return true;
            }
            return false;
        });

        sshd.start();
        System.out.println("Started SFTP server with root path: " + tempSftpDir.toFile().getAbsolutePath());
        return new SftpServerBean(sshd, tempSftpDir);
    }

