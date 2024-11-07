package de.jmizv.musicvault.domain.hash;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class HashService {

    private final MessageDigest _messageDigest;
    private final Base64.Encoder _base64Encoder = Base64.getEncoder();

    public HashService() throws NoSuchAlgorithmException {
        _messageDigest = MessageDigest.getInstance("SHA-256");
    }

    public String hash(Path path) throws IOException {
        return hash(Files.readAllBytes(path));
    }

    public String hash(byte[] array) {
        return new String(_base64Encoder.encode(_messageDigest.digest(array)));
    }
}
