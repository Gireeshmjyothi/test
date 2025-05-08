@Test
void testCreateAcknowledgmentFile_Success() throws Exception {
    // Set mock ChannelSftp
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String fileName = "report123";
    String filePath = "/remote/ack";
    boolean success = true;

    // Simulate upload success
    Mockito.doNothing().when(channelSftp).put(Mockito.anyString(), Mockito.anyString());

    assertDoesNotThrow(() -> sftpClient.createAcknowledgmentFile(fileName, success, filePath));

    // Verify that 'put' is called with proper destination path
    Mockito.verify(channelSftp).put(Mockito.anyString(), Mockito.contains("report123_processed.txt"));
}

@Test
void testCreateAcknowledgmentFile_FailureInPut() throws Exception {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String fileName = "data";
    String filePath = "/remote/ack";
    boolean success = false;

    Mockito.doThrow(new SftpException(4, "Permission denied"))
           .when(channelSftp).put(Mockito.anyString(), Mockito.anyString());

    SftpException ex = assertThrows(SftpException.class, () ->
            sftpClient.createAcknowledgmentFile(fileName, success, filePath));

    assertTrue(ex.getMessage().contains("Failed to create acknowledgment file"));
    assertEquals(ChannelSftp.SSH_FX_FAILURE, ex.id);
}

@Test
void testCreateAcknowledgmentFile_ChannelSftpNull() {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", null);

    String fileName = "ack";
    String filePath = "/remote/ack";
    boolean success = true;

    NullPointerException ex = assertThrows(NullPointerException.class, () ->
            sftpClient.createAcknowledgmentFile(fileName, success, filePath));

    assertNotNull(ex.getMessage());
}
