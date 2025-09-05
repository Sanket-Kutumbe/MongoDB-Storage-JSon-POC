package audit.aggregator.demo.controller;

import audit.aggregator.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        return service.uploadAuditTrailFile(file);
    }


}
