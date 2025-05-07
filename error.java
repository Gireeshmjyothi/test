import org.apache.sshd.common.config.keys.KeyUtils;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.common.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.ProcessShellCommandFactory;
import org.apache.sshd.server.config.keys.DefaultAuthorizedKeysAuthenticator;
import org.apache.sshd.server.config.keys.DefaultHostKeyProvider;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.apache.sshd.common.signature.BuiltinSignatures;
import org.apache.sshd.common.signature.SignatureFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public static SftpServerBean setupSftpServer(String username, String password, int port) throws IOException {
    Path tempSftpDir = Paths.get("C:\\SFTP");

    // Create a stronger host key provider (e.g., ECDSA or RSA)
    SimpleGeneratorHostKeyProvider hostKeyProvider = new SimpleGeneratorHostKeyProvider(Paths.get("hostkey.ser"));
    hostKeyProvider.setAlgorithm("RSA"); // You can also use "EC" for ECDSA

    List<NamedFactory<UserAuth>> userAuthFactories = List.of(new UserAuthPasswordFactory());
    List<NamedFactory<Command>> sftpCommandFactory = List.of(new SftpSubsystemFactory());

    SshServer sshd = SshServer.setUpDefaultServer();
    sshd.setPort(port);
    sshd.setKeyPairProvider(hostKeyProvider);
    sshd.setUserAuthFactories(userAuthFactories);
    sshd.setCommandFactory(new ProcessShellCommandFactory());
    sshd.setSubsystemFactories(sftpCommandFactory);

    // Set supported signature algorithms (compatible with JSCH 0.2.25)
    sshd.setSignatureFactories(Arrays.asList(
        BuiltinSignatures.rsaSha512,
        BuiltinSignatures.rsaSha256,
        BuiltinSignatures.ecdsaSha2Nistp256
    ));

    sshd.setPasswordAuthenticator((usernameAuth, passwordAuth, session) -> {
        if (username.equals(usernameAuth) && password.equals(passwordAuth)) {
            sshd.setFileSystemFactory(new VirtualFileSystemFactory(tempSftpDir));
            return true;
        }
        return false;
    });

    sshd.start();
    System.out.println("Started SFTP server with root path: " + tempSftpDir.toAbsolutePath());

    return new SftpServerBean(sshd, tempSftpDir);
}
