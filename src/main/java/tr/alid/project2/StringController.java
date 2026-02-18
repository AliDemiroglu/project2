package tr.alid.project2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@RestController
@RequestMapping("/api")
public class StringController {

    @Value("${file.output-path}")
    private String outputPath;

    @PostMapping("/save")
    public ResponseEntity<String> saveString(@RequestBody String input) {
        try {
            // outputPath'ten dosya yolu al
            Path filePath = Paths.get(outputPath);

            // Dosyaya yaz
            Files.write(filePath, (input + System.lineSeparator()).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            return ResponseEntity.ok("String başarıyla kaydedildi: " + input + " -> " + outputPath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hata oluştu: " + e.getMessage());
        }
    }
}