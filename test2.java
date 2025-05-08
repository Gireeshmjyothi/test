@Test
void testFindListOfFilesIgnoresDotAndDotDot() throws SftpException {
    String subfolder = "/folder";
    List<String> subfolders = List.of(subfolder);

    // Mock '.' entry
    ChannelSftp.LsEntry dotEntry = mock(ChannelSftp.LsEntry.class);
    SftpATTRS dotAttrs = mock(SftpATTRS.class);
    when(dotEntry.getFilename()).thenReturn(".");
    when(dotEntry.getAttrs()).thenReturn(dotAttrs);
    when(dotAttrs.isDir()).thenReturn(false);

    // Mock '..' entry
    ChannelSftp.LsEntry dotDotEntry = mock(ChannelSftp.LsEntry.class);
    SftpATTRS dotDotAttrs = mock(SftpATTRS.class);
    when(dotDotEntry.getFilename()).thenReturn("..");
    when(dotDotEntry.getAttrs()).thenReturn(dotDotAttrs);
    when(dotDotAttrs.isDir()).thenReturn(false);

    Vector<ChannelSftp.LsEntry> fileVector = new Vector<>();
    fileVector.add(dotEntry);
    fileVector.add(dotDotEntry);

    when(channelSftp.ls(subfolder)).thenReturn(fileVector);

    List<FileInfo> result = sftpClient.findListOfFiles(subfolders);

    // Assert that these dot entries are ignored
    assertNotNull(result);
    assertTrue(result.isEmpty(), "Dot and dot-dot entries should be ignored");
}
