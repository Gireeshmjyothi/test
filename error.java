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


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SftpClient {

    private Session session;
    private ChannelSftp channelSftp;

    public void connect(String host, int port, String username, String password) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(username, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no"); // For testing purposes
        session.connect();

        channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        System.out.println("Connected to SFTP server at " + host + ":" + port);
    }

    public void disconnect() {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        System.out.println("Disconnected from SFTP server");
    }

    public static void main(String[] args) {
        SftpClient client = new SftpClient();
        try {
            client.connect("localhost", 2222, "testuser", "testpass");
            // Perform file operations here
            client.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
