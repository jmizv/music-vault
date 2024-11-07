package de.jmizv.musicvault;


import de.jmizv.musicvault.infrastructure.FileObjectService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@SpringBootApplication
public class MainApplication {

    public MainApplication(FileObjectService fileObjectService, @Value("${base-path}") String basePath) throws IOException {
        basePath = Path.of(basePath).toAbsolutePath().toString();

        fileObjectService.checkMissingFiles(basePath);
        fileObjectService.checkConsistencyOfFiles(basePath);
        fileObjectService.checkForNewFiles(basePath);

        try (var pw = new PrintWriter(new FileWriter("c:\\Temp\\all.m3u8", StandardCharsets.UTF_8))) {
            fileObjectService.writeM3uWithShortestTracksOfEachArtist(basePath, pw);
            pw.flush();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
