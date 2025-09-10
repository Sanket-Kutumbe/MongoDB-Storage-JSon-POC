package audit.aggregator.demo.controller;

import audit.aggregator.demo.service.S3Service;
import audit.aggregator.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private StorageService service;

    @GetMapping("/getTest")
    public String getTest() {
        return "Get request received";
    }

    @PostMapping("/parseAndStoreFile")
    public ResponseEntity<String> parseAndStoreFile(@RequestParam("file") final MultipartFile file) throws IOException {
        return service.uploadAuditTrailFileToMongo(file.getBytes());
    }

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String url = s3Service.uploadFileToS3(file);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/list")
    public List<String> listFiles(@RequestParam(required = false, defaultValue = "") String prefix) {
        return s3Service.listFiles(prefix);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String key) throws Exception {

        byte[] data = s3Service.getFile(key);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

    @GetMapping("/loadFile")
    public String loadFile(@RequestParam String key) throws Exception {
        s3Service.loadFile(key);
        return "File loaded successfully";
    }
}
