@ExtendWith(MockitoExtension.class)
class SftpClientTest {

    @Mock
    private JSch jsch;

    @Mock
    private Session session;

    @Mock
    private ChannelSftp channelSftp;

    private SftpClient sftpClient;

    private final String host = "localhost";
    private final int port = 22;
    private final String username = "testUser";
    private final String password = "testPass";

    @BeforeEach
    void setUp() throws JSchException {
        Mockito.when(jsch.getSession(username, host, port)).thenReturn(session);
        Mockito.when(session.openChannel("sftp")).thenReturn(channelSftp);

        sftpClient = new SftpClient(host, port, username, password, jsch);
    }

    @Test
    void testConnect_Success() throws Exception {
        Mockito.doNothing().when(session).connect();
        Mockito.doNothing().when(channelSftp).connect();

        assertDoesNotThrow(() -> sftpClient.connect());
        Mockito.verify(session).connect();
        Mockito.verify(channelSftp).connect();
    }

    @Test
    void testConnect_SessionConnectionFails() throws Exception {
        Mockito.doThrow(new JSchException("Session failed")).when(session).connect();

        JSchException ex = assertThrows(JSchException.class, () -> sftpClient.connect());
        assertEquals("Session failed", ex.getMessage());
    }

    @Test
    void testConnect_ChannelOpenFails() throws Exception {
        Mockito.when(session.openChannel("sftp")).thenThrow(new JSchException("Channel failed"));
        Mockito.doNothing().when(session).connect();

        JSchException ex = assertThrows(JSchException.class, () -> sftpClient.connect());
        assertEquals("Channel failed", ex.getMessage());
    }

    @Test
    void testConnect_ChannelSftpConnectFails() throws Exception {
        Mockito.doNothing().when(session).connect();
        Mockito.doThrow(new JSchException("SFTP connect failed")).when(channelSftp).connect();

        JSchException ex = assertThrows(JSchException.class, () -> sftpClient.connect());
        assertEquals("SFTP connect failed", ex.getMessage());
    }
                     }
