@ExtendWith(MockitoExtension.class)
class SftpClientServiceTest {

    @Mock
    private SftpClientHelper sftpClient;

    @InjectMocks
    private SftpClientService sftpClientService;

    private final FileInfo file1 = new FileInfo("file1.txt", "/RnS/SBI", "/RnS/SBI/file1.txt", System.currentTimeMillis());

    @BeforeEach
    void setup() {
        // Common setup if needed
    }
}

@Test
void testFindListOfFiles_NoFilesFound() throws Exception {
    when(sftpClient.findListOfFiles(anyList())).thenReturn(List.of());

    sftpClientService.findListOfFiles();

    verify(sftpClient).connect();
    verify(sftpClient).disconnect();
    verify(sftpClient).findListOfFiles(anyList());
    verifyNoMoreInteractions(sftpClient);
}

@Test
void testFindListOfFiles_OneFileProcessedSuccessfully() throws Exception {
    when(sftpClient.findListOfFiles(anyList())).thenReturn(List.of(file1));
    doNothing().when(sftpClient).downloadFile(eq(file1.getRemotePath()), anyString());
    doNothing().when(sftpClient).moveFile(eq(file1.getRemotePath()), anyString());
    doNothing().when(sftpClient).createAcknowledgmentFile(eq(file1.getFileName()), eq(true), anyString());

    sftpClientService.findListOfFiles();

    verify(sftpClient).connect();
    verify(sftpClient).findListOfFiles(anyList());
    verify(sftpClient).downloadFile(eq(file1.getRemotePath()), anyString());
    verify(sftpClient).moveFile(eq(file1.getRemotePath()), contains("processed"));
    verify(sftpClient).createAcknowledgmentFile(eq(file1.getFileName()), eq(true), anyString());
    verify(sftpClient).disconnect();
}

@Test
void testFindListOfFiles_DownloadFails() throws Exception {
    when(sftpClient.findListOfFiles(anyList())).thenReturn(List.of(file1));
    doThrow(new SftpException(0, "download failed")).when(sftpClient).downloadFile(eq(file1.getRemotePath()), anyString());
    doNothing().when(sftpClient).createAcknowledgmentFile(eq(file1.getFileName()), eq(false), anyString());

    sftpClientService.findListOfFiles();

    verify(sftpClient).downloadFile(eq(file1.getRemotePath()), anyString());
    verify(sftpClient, never()).moveFile(anyString(), anyString());
    verify(sftpClient).createAcknowledgmentFile(eq(file1.getFileName()), eq(false), anyString());
}

@Test
void testFindListOfFiles_AckFileCreationFails() throws Exception {
    when(sftpClient.findListOfFiles(anyList())).thenReturn(List.of(file1));
    doNothing().when(sftpClient).downloadFile(anyString(), anyString());
    doNothing().when(sftpClient).moveFile(anyString(), anyString());
    doThrow(new SftpException(0, "ack failed")).when(sftpClient)
        .createAcknowledgmentFile(eq(file1.getFileName()), eq(true), anyString());

    sftpClientService.findListOfFiles();

    verify(sftpClient).createAcknowledgmentFile(eq(file1.getFileName()), eq(true), anyString());
}

@Test
void testFindListOfFiles_ConnectionFails() throws Exception {
    doThrow(new JSchException("connection failed")).when(sftpClient).connect();

    sftpClientService.findListOfFiles();

    verify(sftpClient).connect();
    verify(sftpClient).disconnect();
    verifyNoMoreInteractions(sftpClient);
}


