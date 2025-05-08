@Test
void testUploadFile_Success() throws Exception {
    String localPath = "testfile.txt";
    String remotePath = "/remote/testfile.txt";

    File file = new File(localPath);
    Files.writeString(file.toPath(), "sample data"); // create temporary file

    sftpClient.uploadFile(localPath, remotePath);

    verify(channelSftp).put(localPath, remotePath);

    // cleanup
    Files.deleteIfExists(file.toPath());
}

@Test
void testUploadFile_FileDoesNotExist() {
    String localPath = "nonexistent.txt";
    String remotePath = "/remote/nonexistent.txt";

    SftpException exception = assertThrows(SftpException.class, () ->
        sftpClient.uploadFile(localPath, remotePath)
    );

    assertEquals(ChannelSftp.SSH_FX_NO_SUCH_FILE, exception.id);
    assertTrue(exception.getMessage().contains("Local file does not exist"));
}
