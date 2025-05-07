import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.common.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.common.session.SessionContext;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.common.signature.BuiltinSignatures;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.Collections;
import java.util.List;

public class LocalSftpServer {

    public static SshServer startSftpServer(String username, String password, int port) throws IOException {
        Path homeDir = Paths.get("C:/SFTP"); // Make sure this exists

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);

        // Provide host key (persisted to file)
        KeyPairProvider hostKeyProvider = new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser"));
        sshd.setKeyPairProvider(hostKeyProvider);

        // Set signature algorithms compatible with JSCH 0.2.25
        sshd.setSignatureFactories(BuiltinSignatures.resolveSignatureFactories(
                "rsa-sha2-512,rsa-sha2-256,ssh-ed25519"
        ));

        // Password authentication
        sshd.setPasswordAuthenticator((user, pass, session) ->
                username.equals(user) && password.equals(pass)
        );

        // SFTP subsystem
        sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));

        // Optional: SCP support
        sshd.setCommandFactory(new ScpCommandFactory());

        // Virtual file system
        sshd.setFileSystemFactory(new VirtualFileSystemFactory(homeDir));

        sshd.start();
        System.out.println("SFTP Server started on port " + port + " with root: " + homeDir);
        return sshd;
    }
}

public void connect() throws JSchException {
    log.info("Preparing SFTP Connection.");
    JSch jsch = new JSch();
    session = jsch.getSession(username, host, port);
    session.setPassword(password);

    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    config.put("server_host_key", "rsa-sha2-512,rsa-sha2-256,ssh-ed25519"); // Optional, but good practice
    session.setConfig(config);

    log.info("Connecting to " + host + ":" + port + " with user " + username);
    session.connect();
    channelSftp = (ChannelSftp) session.openChannel("sftp");
    log.info("Connecting to SFTP.");
    channelSftp.connect();
    log.info("Connected to SFTP server.");
}
