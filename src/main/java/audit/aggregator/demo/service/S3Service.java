package audit.aggregator.demo.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Autowired
    private StorageService storageService;

    public String uploadFileToS3(MultipartFile file) throws IOException {
//        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String prefix = "audit-sanket/"; // Optional: specify a folder within the bucket
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(prefix)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucketName + ".s3.amazonaws.com/" + prefix;
    }

    public List<String> listFiles(String prefix) {
        ListObjectsV2Request listObjects = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix) // pass "" if you want all files
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(listObjects);

        return response.contents()
                .stream()
                .map(S3Object::key) // extract only the keys (file paths)
                .collect(Collectors.toList());
    }

    public byte[] getFile(String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest)) {
            return s3Object.readAllBytes(); // returns file as byte[]
        }
    }

    public void downloadFile(String key, Path destination) throws IOException {
        byte[] data = getFile(key);
        Files.write(destination, data);
    }

    public void loadFile(final String key) throws IOException {
        byte[] data = getFile(key);
        storageService.uploadAuditTrailFileToMongo(data);
    }
}
