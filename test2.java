@Test
void testFindListOfFilesSkipsDirectories() throws SftpException {
    String subfolder = "/folder";
    List<String> subfolders = List.of(subfolder);

    ChannelSftp.LsEntry dirEntry = mock(ChannelSftp.LsEntry.class);
    SftpATTRS attrs = mock(SftpATTRS.class);
    when(dirEntry.getAttrs()).thenReturn(attrs);
    when(attrs.isDir()).thenReturn(true);

    Vector<ChannelSftp.LsEntry> fileVector = new Vector<>();
    fileVector.add(dirEntry);

    when(channelSftp.ls(subfolder)).thenReturn(fileVector);

    List<FileInfo> result = sftpClient.findListOfFiles(subfolders);
    assertTrue(result.isEmpty(), "Directory entries should be skipped");
}
