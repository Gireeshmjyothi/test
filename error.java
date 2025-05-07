import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.ProcessShellCommandFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class LocalSftpServer {

    public static void main(String[] args) throws IOException {
        setupSftpServer("testuser", "testpass", 2222);
    }

    public static void setupSftpServer(String username, String password, int port) throws IOException {
        Path rootDir = Paths.get("C:/SFTP"); // Ensure this directory exists
        Path hostKeyPath = Paths.get("hostkey.ser");

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(hostKeyPath));
        sshd.setPasswordAuthenticator((u, p, session) -> u.equals(username) && p.equals(password));
        sshd.setUserAuthFactories(Collections.singletonList(new UserAuthPasswordFactory()));
        sshd.setCommandFactory(new ProcessShellCommandFactory());
        sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
        sshd.setFileSystemFactory(new VirtualFileSystemFactory(rootDir));
        sshd.setSignatureFactories(SecurityUtils.getRegisteredSignatures());

        sshd.start();
        System.out.println("SFTP server started at port " + port + ", root: " + rootDir);
    }
}
