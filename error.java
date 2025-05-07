import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.common.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.common.session.helpers.DefaultSessionFactory;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.apache.sshd.server.config.keys.DefaultAuthorizedKeysAuthenticator;
import org.apache.sshd.server.keyprovider.HostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.Collections;
import java.util.List;

public class SftpServer {

    public static SshServer startSftpServer(String username, String password, int port, String rootDirPath) throws IOException {
        Path rootDir = Paths.get(rootDirPath);
        SshServer sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);

        // Default key provider (RSA)
        HostKeyProvider keyPairProvider = new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser"));
        sshd.setKeyPairProvider(keyPairProvider);

        // Default authentication
        sshd.setPasswordAuthenticator((u, p, session) -> username.equals(u) && password.equals(p));

        // Default SFTP subsystem
        List<CommandFactory> subsystemFactories = Collections.singletonList(new SftpSubsystemFactory());
        sshd.setSubsystemFactories(subsystemFactories);

        // Set file system root
        sshd.setFileSystemFactory(new VirtualFileSystemFactory(rootDir));

        // Use default signature algorithms (JSCH 0.2.25 supports these by default)
        sshd.setSignatureFactories(SecurityUtils.getRegisteredProvider(org.apache.sshd.common.signature.SignatureFactory.class));

        sshd.start();
        System.out.println("SFTP server started at port " + port + ", root dir: " + rootDir.toAbsolutePath());
        return sshd;
    }
}
