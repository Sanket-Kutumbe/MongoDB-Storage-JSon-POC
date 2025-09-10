package audit.aggregator.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StorageService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ResponseEntity<String> uploadAuditTrailFileToMongo(final byte[] json) {
        try {

            // Parse JSON array into List of Maps
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> auditLogs =
                    objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});

            // Save all documents into "audit-trail-collection-2"
            mongoTemplate.insert(auditLogs, "audit-trail-collection-3");
            return ResponseEntity.ok("File uploaded and data stored successfully. Count: " + auditLogs.size());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}

