@Test
void testFindListOfFiles_SuccessWithValidFiles() throws SftpException {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String subfolder = "/remote/folder";
    List<String> subfolders = List.of(subfolder);

    ChannelSftp.LsEntry fileEntry = Mockito.mock(ChannelSftp.LsEntry.class);
    SftpATTRS attrs = Mockito.mock(SftpATTRS.class);

    Mockito.when(fileEntry.getFilename()).thenReturn("data.csv");
    Mockito.when(fileEntry.getAttrs()).thenReturn(attrs);
    Mockito.when(attrs.isDir()).thenReturn(false);
    Mockito.when(attrs.getMTime()).thenReturn((int) (System.currentTimeMillis() / 1000));

    Vector<ChannelSftp.LsEntry> fileVector = new Vector<>();
    fileVector.add(fileEntry);

    Mockito.when(channelSftp.ls(subfolder)).thenReturn(fileVector);

    List<FileInfo> result = sftpClient.findListOfFiles(subfolders);

    assertEquals(1, result.size());
    FileInfo fileInfo = result.get(0);
    assertEquals("data.csv", fileInfo.getFileName());
    assertEquals("/remote/folder/data.csv", fileInfo.getRemotePath());
}

@Test
void testFindListOfFiles_EmptyFolder() throws SftpException {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String subfolder = "/empty/folder";
    List<String> subfolders = List.of(subfolder);
    Mockito.when(channelSftp.ls(subfolder)).thenReturn(new Vector<>());

    List<FileInfo> result = sftpClient.findListOfFiles(subfolders);
    assertTrue(result.isEmpty());
}

@Test
void testFindListOfFiles_IgnoresDotAndDotDot() throws SftpException {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String subfolder = "/folder";
    List<String> subfolders = List.of(subfolder);

    ChannelSftp.LsEntry dotEntry = Mockito.mock(ChannelSftp.LsEntry.class);
    Mockito.when(dotEntry.getFilename()).thenReturn(".");

    ChannelSftp.LsEntry dotDotEntry = Mockito.mock(ChannelSftp.LsEntry.class);
    Mockito.when(dotDotEntry.getFilename()).thenReturn("..");

    Vector<ChannelSftp.LsEntry> fileVector = new Vector<>();
    fileVector.add(dotEntry);
    fileVector.add(dotDotEntry);

    Mockito.when(channelSftp.ls(subfolder)).thenReturn(fileVector);

    List<FileInfo> result = sftpClient.findListOfFiles(subfolders);
    assertTrue(result.isEmpty());
}

@Test
void testFindListOfFiles_SkipsDirectories() throws SftpException {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String subfolder = "/folder";
    List<String> subfolders = List.of(subfolder);

    ChannelSftp.LsEntry dirEntry = Mockito.mock(ChannelSftp.LsEntry.class);
    SftpATTRS attrs = Mockito.mock(SftpATTRS.class);
    Mockito.when(dirEntry.getFilename()).thenReturn("subdir");
    Mockito.when(dirEntry.getAttrs()).thenReturn(attrs);
    Mockito.when(attrs.isDir()).thenReturn(true);

    Vector<ChannelSftp.LsEntry> fileVector = new Vector<>();
    fileVector.add(dirEntry);

    Mockito.when(channelSftp.ls(subfolder)).thenReturn(fileVector);

    List<FileInfo> result = sftpClient.findListOfFiles(subfolders);
    assertTrue(result.isEmpty());
}

@Test
void testFindListOfFiles_SubfolderThrowsException() throws SftpException {
    ReflectionTestUtils.setField(sftpClient, "channelSftp", channelSftp);

    String subfolder1 = "/valid";
    String subfolder2 = "/error";
    List<String> subfolders = List.of(subfolder1, subfolder2);

    // Valid folder
    ChannelSftp.LsEntry entry = Mockito.mock(ChannelSftp.LsEntry.class);
    SftpATTRS attrs = Mockito.mock(SftpATTRS.class);
    Mockito.when(entry.getFilename()).thenReturn("file.txt");
    Mockito.when(entry.getAttrs()).thenReturn(attrs);
    Mockito.when(attrs.isDir()).thenReturn(false);
    Mockito.when(attrs.getMTime()).thenReturn((int) (System.currentTimeMillis() / 1000));
    Vector<ChannelSftp.LsEntry> vec = new Vector<>();
    vec.add(entry);

    Mockito.when(channelSftp.ls(subfolder1)).thenReturn(vec);
    Mockito.when(channelSftp.ls(subfolder2)).thenThrow(new SftpException(4, "Access denied"));

    List<FileInfo> result = sftpClient.findListOfFiles(subfolders);

    assertEquals(1, result.size());
    assertEquals("file.txt", result.get(0).getFileName());
}
