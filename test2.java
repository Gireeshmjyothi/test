@ExtendWith(MockitoExtension.class)
class SftpClientTest {

    @Mock
    JSch jsch;

    @Mock
    Session session;

    @Mock
    ChannelSftp channelSftp;

    @Mock
    Channel channel;

    private SftpClient sftpClient;

    private final String host = "localhost";
    private final int port = 22;
    private final String username = "user";
    private final String password = "pass";

    @BeforeEach
    void setUp() {
        sftpClient = new SftpClient(host, port, username, password) {
            @Override
            public void connect() throws JSchException {
                // Inject mocked JSch
                session = SftpClientTest.this.session;
                channelSftp = SftpClientTest.this.channelSftp;

                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                Mockito.when(session.openChannel("sftp")).thenReturn(channelSftp);
                channelSftp.connect();
            }
        };
    }

    @Test
    void testConnect_Success() throws Exception {
        Mockito.doNothing().when(session).setPassword(password);
        Mockito.doNothing().when(session).setConfig(Mockito.anyString(), Mockito.anyString());
        Mockito.doNothing().when(session).connect();
        Mockito.when(session.openChannel("sftp")).thenReturn(channelSftp);
        Mockito.doNothing().when(channelSftp).connect();

        assertDoesNotThrow(() -> sftpClient.connect());
    }

    @Test
    void testConnect_SessionConnectionFails() throws Exception {
        Mockito.doThrow(new JSchException("Session failed")).when(session).connect();

        JSchException thrown = assertThrows(JSchException.class, () -> sftpClient.connect());
        assertEquals("Session failed", thrown.getMessage());
    }

    @Test
    void testConnect_OpenChannelFails() throws Exception {
        Mockito.doNothing().when(session).connect();
        Mockito.doThrow(new JSchException("Channel open failed")).when(session).openChannel("sftp");

        JSchException thrown = assertThrows(JSchException.class, () -> {
            session.openChannel("sftp");
        });
        assertEquals("Channel open failed", thrown.getMessage());
    }

    @Test
    void testConnect_ChannelSftpConnectFails() throws Exception {
        Mockito.doNothing().when(session).connect();
        Mockito.when(session.openChannel("sftp")).thenReturn(channelSftp);
        Mockito.doThrow(new JSchException("SFTP connect failed")).when(channelSftp).connect();

        JSchException thrown = assertThrows(JSchException.class, () -> {
            channelSftp.connect();
        });
        assertEquals("SFTP connect failed", thrown.getMessage());
    }
}
