public List<FileInfo> findListOfFiles(List<String> remoteSubfolders) throws SftpException {
        List<FileInfo> recentFiles = new ArrayList<>();

        for (String subfolder : remoteSubfolders) {
            try {
                Vector<ChannelSftp.LsEntry> files = channelSftp.ls(subfolder);
                for (ChannelSftp.LsEntry entry : files) {
                    if (!entry.getAttrs().isDir() && !entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
                        long mTime = entry.getAttrs().getMTime() * 1000L; // Convert to milliseconds
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setFolder(subfolder);
                        fileInfo.setFileName(entry.getFilename());
                        fileInfo.setRemotePath(subfolder + SFTP_FILE_SEPARATOR + entry.getFilename());
                        fileInfo.setModificationTime(mTime);
                        recentFiles.add(fileInfo);
                    }
                }
            } catch (SftpException e) {
                log.error("Error while listing files : {}", e.getMessage());
            }
        }
        return recentFiles;
    }
