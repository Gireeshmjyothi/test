


import com.epay.rns.entity.FileInfo;
import com.jcraft.jsch.*;
import com.sbi.epay.logging.utility.LoggerFactoryUtility;
import com.sbi.epay.logging.utility.LoggerUtility;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@RequiredArgsConstructor
public class SftpClientUtil {

    private final String host;
    private final String username;
    private final String password;
    private final int port;
    LoggerUtility log = LoggerFactoryUtility.getLogger(this.getClass());
    private Session session;
    private ChannelSftp channelSftp;

    public void connect() throws JSchException {
        log.info("Preparing SFTP Connection.");
        JSch jsch = new JSch();
        session = jsch.getSession(username, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no"); // Disable host key checking for testing
        log.info("Connecting to " + host + ":" + port + " with user " + username);
        session.connect();
        channelSftp = (ChannelSftp) session.openChannel("sftp");
        log.info("Connecting to SFTP.");
        channelSftp.connect();
        log.info("Connected to SFTP server.");
    }
}
