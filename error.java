String cleanBase = baseRemoteDir.replaceAll("^/+", "").replaceAll("/+$", ""); // 'upload'
String cleanSub = subPath != null ? subPath.replaceAll("^/+", "").replaceAll("/+$", "") : "";
String fullPath = cleanSub.isEmpty() ? cleanBase : cleanBase + "/" + cleanSub;

// ✅ This will be like: 'upload/HDFC'
createDirectoriesIfNotExist(sftp, fullPath); // safe, step-by-step
