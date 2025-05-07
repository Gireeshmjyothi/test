import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.apache.sshd.common.NamedFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class LocalSftpServer {

    public static SshServer startServer(String username, String password, int port, String rootDirPath) throws IOException {
        Path rootDir = Paths.get(rootDirPath);
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);

        // Generate an RSA host key and store in hostkey.ser
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser")));

        // Set user authentication
        sshd.setPasswordAuthenticator((u, p, session) -> username.equals(u) && password.equals(p));

        // Set up SFTP subsystem
        List<NamedFactory<Command>> sftpSubsystem = Collections.singletonList(new SftpSubsystemFactory());
        sshd.setSubsystemFactories(sftpSubsystem);

        // Set root directory
        sshd.setFileSystemFactory(new VirtualFileSystemFactory(rootDir));

        // Use default signature algorithms (ed25519, ecdsa, rsa-sha2-256, etc.)
        sshd.setSignatureFactories(SecurityUtils.getRegisteredProvider(org.apache.sshd.common.signature.SignatureFactory.class));

        sshd.start();
        System.out.println("SFTP Server started on port " + port + " with root at " + rootDir.toAbsolutePath());
        return sshd;
    }

    public static void main(String[] args) throws IOException {
        startServer("testuser", "testpass", 2222, "C:\\SFTP");
    }
    }
