@Test
    void testFindListOfFilesSkipsDirectories() throws SftpException {
        String subfolder = "/folder";
        List<String> subfolders = List.of(subfolder);

        ChannelSftp.LsEntry dirEntry = mock(ChannelSftp.LsEntry.class);
        SftpATTRS attrs = mock(SftpATTRS.class);
        when(dirEntry.getFilename()).thenReturn("subdir");
        when(dirEntry.getAttrs()).thenReturn(attrs);
        when(attrs.isDir()).thenReturn(true);

        Vector<ChannelSftp.LsEntry> fileVector = new Vector<>();
        fileVector.add(dirEntry);

        when(channelSftp.ls(subfolder)).thenReturn(fileVector);

        List<FileInfo> result = sftpClient.findListOfFiles(subfolders);
        assertTrue(result.isEmpty());
    }


Unnecessary stubbings detected.
Clean & maintainable test code requires zero unnecessary code.
Following stubbings are unnecessary (click to navigate to relevant line of code):
  1. -> at com.epay.rns.externalservice.SftpClientHelperTest.testFindListOfFilesSkipsDirectories(SftpClientHelperTest.java:338)
Please remove unnecessary stubbings or use 'lenient' strictness. More info: javadoc for UnnecessaryStubbingException class.
org.mockito.exceptions.misusing.UnnecessaryStubbingException: 
Unnecessary stubbings detected.
Clean & maintainable test code requires zero unnecessary code.
Following stubbings are unnecessary (click to navigate to relevant line of code):
  1. -> at com.epay.rns.externalservice.SftpClientHelperTest.testFindListOfFilesSkipsDirectories(SftpClientHelperTest.java:338)
Please remove unnecessary stubbings or use 'lenient' strictness. More info: javadoc for UnnecessaryStubbingException class.
	at org.mockito.junit.jupiter.MockitoExtension.afterEach(MockitoExtension.java:197)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)


OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
