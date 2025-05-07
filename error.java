import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

public class EmbeddedSftpServer {

    public static void main(String[] args) {
        try {
            startSftpServer("test", "test", 2222, "C:/SFTP");
        } catch (Exception e) {
            System.err.println("Failed to start SFTP server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void startSftpServer(String username, String password, int port, String rootDir) throws IOException, InterruptedException {
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser")));
        sshd.setPasswordAuthenticator((u, p, session) -> u.equals(username) && p.equals(password));
        sshd.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
        sshd.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get(rootDir)));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Shutting down SFTP server...");
                sshd.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        sshd.start();
        System.out.println("SFTP server started on port " + port + ", root: " + rootDir);
        Thread.currentThread().join(); // Keep running
    }
}
