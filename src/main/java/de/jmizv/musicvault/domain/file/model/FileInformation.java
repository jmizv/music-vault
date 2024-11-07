package de.jmizv.musicvault.domain.file.model;

import java.util.Map;

public record FileInformation(String fileName,
                              String path,
                              long fileSize,
                              long lastChanged,
                              String hash,
                              Map<String, Object> metadata) {
}
