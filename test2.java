@Test
void testMoveFile_Success() throws Exception {
    // Set the mock channelSftp
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String source = "/remote/source.txt";
    String destination = "/remote/destination.txt";

    Mockito.doNothing().when(channelSftp).rename(source, destination);

    assertDoesNotThrow(() -> sftpClient.moveFile(source, destination));

    Mockito.verify(channelSftp).rename(source, destination);
}

@Test
void testMoveFile_Failure() throws Exception {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String source = "/remote/source.txt";
    String destination = "/remote/destination.txt";

    SftpException originalException = new SftpException(4, "Permission denied");

    Mockito.doThrow(originalException).when(channelSftp).rename(source, destination);

    SftpException thrown = assertThrows(SftpException.class, () ->
            sftpClient.moveFile(source, destination));

    assertTrue(thrown.getMessage().contains("Failed to move file"));
    assertEquals(4, thrown.id);
}

@Test
void testMoveFile_ChannelSftpNull() {
    // Explicitly set channelSftp to null
    ReflectionTestUtils.setField(sftpClient, "channelSftp", null);

    String source = "/remote/source.txt";
    String destination = "/remote/destination.txt";

    NullPointerException ex = assertThrows(NullPointerException.class, () ->
            sftpClient.moveFile(source, destination));

    assertNotNull(ex.getMessage()); // optional: just to assert something was thrown
}
