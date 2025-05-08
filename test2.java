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
        sftpClient = new SftpClient(host, port, username, password, jsch);
        // Setting the private fields for disconnect testing
        ReflectionTestUtils.setField(sftpClient, "session", session);
        ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);
    }

    @Test
    void testDisconnect_BothConnected() {
        Mockito.when(channelSftp.isConnected()).thenReturn(true);
        Mockito.when(session.isConnected()).thenReturn(true);

        sftpClient.disconnect();

        Mockito.verify(channelSftp).disconnect();
        Mockito.verify(session).disconnect();
    }

    @Test
    void testDisconnect_ChannelOnlyConnected() {
        Mockito.when(channelSftp.isConnected()).thenReturn(true);
        Mockito.when(session.isConnected()).thenReturn(false);

        sftpClient.disconnect();

        Mockito.verify(channelSftp).disconnect();
        Mockito.verify(session, Mockito.never()).disconnect();
    }

    @Test
    void testDisconnect_SessionOnlyConnected() {
        Mockito.when(channelSftp.isConnected()).thenReturn(false);
        Mockito.when(session.isConnected()).thenReturn(true);

        sftpClient.disconnect();

        Mockito.verify(channelSftp, Mockito.never()).disconnect();
        Mockito.verify(session).disconnect();
    }

    @Test
    void testDisconnect_NoneConnected() {
        Mockito.when(channelSftp.isConnected()).thenReturn(false);
        Mockito.when(session.isConnected()).thenReturn(false);

        sftpClient.disconnect();

        Mockito.verify(channelSftp, Mockito.never()).disconnect();
        Mockito.verify(session, Mockito.never()).disconnect();
    }

    @Test
    void testDisconnect_NullObjects() {
        ReflectionTestUtils.setField(sftpClient, "channelSftp", null);
        ReflectionTestUtils.setField(sftpClient, "session", null);

        sftpClient.disconnect(); // should not throw exception

        // No exception expected, nothing to verify
    }
}
