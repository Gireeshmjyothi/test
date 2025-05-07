import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.apache.sshd.common.NamedFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

public class LocalSftpServer {

    public static SshServer startServer(String username, String password, int port, String rootDirPath) throws IOException {
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);

        // Generate host key if not present
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser")));

        // Set password authenticator
        sshd.setPasswordAuthenticator((u, p, session) -> username.equals(u) && password.equals(p));

        // Set SFTP subsystem
        sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));

        // Set virtual file system root
        sshd.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get(rootDirPath)));

        sshd.start();
        System.out.println("SFTP Server started on port " + port + ", root directory: " + rootDirPath);
        return sshd;
    }

    public static void main(String[] args) throws IOException {
        startServer("testuser", "testpass", 2222, "C:\\SFTP");
    }
}
