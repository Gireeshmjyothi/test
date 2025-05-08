@Test
void testFindListOfFilesIgnoresDotAndDotDot() throws SftpException {
    String subfolder = "/folder";
    List<String> subfolders = List.of(subfolder);

    // Mock '.' entry
    ChannelSftp.LsEntry dotEntry = mock(ChannelSftp.LsEntry.class);
    when(dotEntry.getFilename()).thenReturn(".");
    SftpATTRS dotAttrs = mock(SftpATTRS.class);
    when(dotAttrs.isDir()).thenReturn(false);  // Still required even if not used due to code logic
    when(dotEntry.getAttrs()).thenReturn(dotAttrs);

    // Mock '..' entry
    ChannelSftp.LsEntry dotDotEntry = mock(ChannelSftp.LsEntry.class);
    when(dotDotEntry.getFilename()).thenReturn("..");
    SftpATTRS dotDotAttrs = mock(SftpATTRS.class);
    when(dotDotAttrs.isDir()).thenReturn(false);
    when(dotDotEntry.getAttrs()).thenReturn(dotDotAttrs);

    Vector<ChannelSftp.LsEntry> fileVector = new Vector<>();
    fileVector.add(dotEntry);
    fileVector.add(dotDotEntry);

    when(channelSftp.ls(subfolder)).thenReturn(fileVector);

    List<FileInfo> result = sftpClient.findListOfFiles(subfolders);
    assertTrue(result.isEmpty());
}
