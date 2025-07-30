import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sftp")
@RequiredArgsConstructor
public class SFTPDownloadController {

    private final SFTPService sftpService;

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam String filePath, @RequestParam String fileName) {
        try {
            byte[] fileData = sftpService.downloadFile(filePath, fileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileData);

        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(404)
                    .body("Error downloading file: " + ex.getMessage());
        }
    }
} u
