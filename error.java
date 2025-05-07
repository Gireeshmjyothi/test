import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LocalSftpServer {

    public static SshServer startServer(String username, String password, int port, String rootDirPath) throws IOException {
        Path rootDir = Paths.get(rootDirPath);

        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);

        // Set HostKey provider
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser")));

        // Password authentication
        sshd.setPasswordAuthenticator((u, p, session) -> username.equals(u) && password.equals(p));

        // SFTP Subsystem
        SftpSubsystemFactory sftpFactory = new SftpSubsystemFactory();
        List<NamedFactory<Command>> sftpSubsystem = sftpFactory.getSubsystemFactories();
        sshd.setSubsystemFactories(sftpSubsystem);

        // Virtual file system
        sshd.setFileSystemFactory(new VirtualFileSystemFactory(rootDir));

        sshd.start();
        System.out.println("SFTP Server started at port " + port + ", root dir: " + rootDir.toAbsolutePath());
        return sshd;
    }

    public static void main(String[] args) throws IOException {
        startServer("testuser", "testpass", 2222, "C:\\SFTP");
    }
}
