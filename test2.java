// Assuming other connect/disconnect test methods exist above

@Test
void testDownloadFile_Success() throws Exception {
    // Set channelSftp via reflection
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String remotePath = "/remote/test.txt";
    String localPath = "/local/test.txt";

    Mockito.doNothing().when(channelSftp).get(remotePath, localPath);

    assertDoesNotThrow(() -> sftpClient.downloadFile(remotePath, localPath));

    Mockito.verify(channelSftp).get(remotePath, localPath);
}

@Test
void testDownloadFile_ThrowsSftpException() throws Exception {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String remotePath = "/remote/missing.txt";
    String localPath = "/local/missing.txt";

    Mockito.doThrow(new SftpException(4, "File not found"))
            .when(channelSftp).get(remotePath, localPath);

    SftpException ex = assertThrows(SftpException.class,
            () -> sftpClient.downloadFile(remotePath, localPath));

    assertEquals("File not found", ex.getMessage());
}

@Test
void testDownloadFile_ChannelSftpNull() {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", null);

    String remotePath = "/remote/file.txt";
    String localPath = "/local/file.txt";

    NullPointerException ex = assertThrows(NullPointerException.class,
            () -> sftpClient.downloadFile(remotePath, localPath));

    assertNotNull(ex.getMessage());
}
