import java.io.*;
import java.nio.file.*;

public class FileUtil {

    // Write InputStream to root directory
    public static String writeToRoot(InputStream inputStream, String fileName) throws IOException {
        // For Linux/Mac: "/" ; For Windows: "C:\\"
        String rootDir = System.getProperty("os.name").toLowerCase().contains("win") ? "C:\\" : "/";
        
        Path filePath = Paths.get(rootDir, fileName);

        // Copy InputStream to file
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toAbsolutePath().toString();
    }

    // Delete file from root directory
    public static void deleteFromRoot(String fileName) throws IOException {
        String rootDir = System.getProperty("os.name").toLowerCase().contains("win") ? "C:\\" : "/";
        Path filePath = Paths.get(rootDir, fileName);

        Files.deleteIfExists(filePath);
    }
}
