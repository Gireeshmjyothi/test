 @Test
    void testFindListOfFilesIgnoresDotAndDotDot() throws SftpException {
        String subfolder = "/folder";
        List<String> subfolders = List.of(subfolder);

        ChannelSftp.LsEntry dotEntry = mock(ChannelSftp.LsEntry.class);
        when(dotEntry.getFilename()).thenReturn(".");

        ChannelSftp.LsEntry dotDotEntry = mock(ChannelSftp.LsEntry.class);
        when(dotDotEntry.getFilename()).thenReturn("..");

        Vector<ChannelSftp.LsEntry> fileVector = new Vector<>();
        fileVector.add(dotEntry);
        fileVector.add(dotDotEntry);

        when(channelSftp.ls(subfolder)).thenReturn(fileVector);

        List<FileInfo> result = sftpClient.findListOfFiles(subfolders);
        assertTrue(result.isEmpty());
    }


Cannot invoke "com.jcraft.jsch.SftpATTRS.isDir()" because the return value of "com.jcraft.jsch.ChannelSftp$LsEntry.getAttrs()" is null
java.lang.NullPointerException: Cannot invoke "com.jcraft.jsch.SftpATTRS.isDir()" because the return value of "com.jcraft.jsch.ChannelSftp$LsEntry.getAttrs()" is null
	at com.epay.rns.externalservice.SftpClientHelper.findListOfFiles(SftpClientHelper.java:95)
	at com.epay.rns.externalservice.SftpClientHelperTest.testFindListOfFilesIgnoresDotAndDotDot(SftpClientHelperTest.java:320)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)


OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
