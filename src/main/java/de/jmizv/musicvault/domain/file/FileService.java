package de.jmizv.musicvault.domain.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.jmizv.musicvault.domain.file.model.FileInformation;
import de.jmizv.musicvault.domain.hash.HashService;
import de.jmizv.musicvault.infrastructure.model.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    private final HashService _hashService;
    private final Gson _gson = new GsonBuilder().create();
    private final String _ffprobePathToExecutable;

    public FileService(HashService hashService, @Value("${ffprobe.path-to-executable}") String ffprobePathToExecutable) {
        _hashService = hashService;
        _ffprobePathToExecutable = ffprobePathToExecutable;
    }

    public Map<String, Object> readMetaData(Path file) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(_ffprobePathToExecutable,
                "-v", "quiet",
                "-print_format", "json",
                "-show_format",
                "-show_streams",
                "\"" + file.toString() + "\"");
        InputStream err;
        InputStream in;
        Process process;
        try {
            process = pb.start();
            err = process.getErrorStream();
            in = process.getInputStream();
            var allBytes = in.readAllBytes();
            var result = new String(allBytes, StandardCharsets.UTF_8);
            var output = _gson.fromJson(result, Map.class);
            if (process.exitValue() != 0) {
                log.error("Exit value: {}\n{}", process.exitValue(), new String(err.readAllBytes()));
            }
            if (!output.containsKey("format")) {
                log.error("There is no format in file {}", file);
                return Map.of();
            }
            var formatMap = (Map<String, Object>) output.get("format");
            formatMap.remove("size");
            formatMap.remove("filename");
            removePrivateTags((Map<String, Object>) formatMap.get("tags"), file);
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void removePrivateTags(Map<String, Object> map, Path path) {
        if (map == null) {
            return;
        }
        Set<String> keysToRemove = map.keySet().stream()
                .filter(key -> key.startsWith("id3v2_priv")).collect(Collectors.toSet());
        for (String key : keysToRemove) {
            map.remove(key);
        }
    }

    public Stream<Path> listFiles(Path path) throws IOException {
        return Files.walk(path).filter(p -> !p.toFile().isDirectory())
                .filter(FileService::checkFileName);
    }

    public long countFiles(Path path) throws IOException {
        return listFiles(path).count();
    }

    public FileInformation fileInformation(Path fileToRead) {
        try {
            var attributes = Files.readAttributes(fileToRead, BasicFileAttributes.class);
            var lastModified = attributes.lastModifiedTime();
            var asFile = fileToRead.toFile();
            var metadata = readMetaData(fileToRead);
            return new FileInformation(asFile.getName(),
                    asFile.getParent(),
                    asFile.length(),
                    lastModified.toMillis(),
                    _hashService.hash(fileToRead),
                    metadata);
        } catch (IOException ex) {
            throw new RuntimeException("Could not read file information attributes", ex);
        }
    }

    private static String asString(Object value) {
        if (value == null) {
            return null;
        }
        return (String) value;
    }

    private static Integer asInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return Integer.parseInt((String) value);
    }

    private static Double asDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        return Double.parseDouble((String) value);
    }

    private static Float asFloat(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        return Float.parseFloat((String) value);
    }

    private static boolean checkFileName(Path f) {
        var fileName = f.toString().toLowerCase();
        // these formats are supported
        var isAudioFile = fileName.endsWith(".mp3")
                          || fileName.endsWith(".mp2")
                          || fileName.endsWith(".wma")
                          || fileName.endsWith(".m4a")
                          || fileName.endsWith(".ogg")
                          || fileName.endsWith(".flac")
                          || fileName.endsWith(".aif");
        if (!isAudioFile) {

            if (// image formats
                    !fileName.endsWith("jpg")
                    && !fileName.endsWith(".jpeg")
                    && !fileName.endsWith(".gif")
                    && !fileName.endsWith(".png")
                    && !fileName.endsWith(".tif")
                    && !fileName.endsWith(".tiff")

                    // Text and other document files
                    && !fileName.endsWith(".ini")
                    && !fileName.endsWith(".txt")
                    && !fileName.endsWith(".pdf")
                    && !fileName.endsWith(".htm")
                    && !fileName.endsWith(".html")
                    && !fileName.endsWith(".doc")
                    && !fileName.endsWith(".odt")
                    && !fileName.endsWith(".docx")
                    && !fileName.endsWith(".rtf")
                    && !fileName.endsWith(".url")

                    // Other stuff
                    && !fileName.endsWith(".message") // temporary
                    && !fileName.endsWith(".cue")
                    && !fileName.endsWith(".m3u")
                    && !fileName.endsWith(".nfo")
                    && !fileName.endsWith(".sfv")
                    && !fileName.endsWith(".gp4")

                    // Video formats
                    && !fileName.endsWith(".wmv")
                    && !fileName.endsWith(".mp4")
                    && !fileName.endsWith(".mpg")
                    && !fileName.endsWith(".mpeg")
                    && !fileName.endsWith(".mov")
                    && !fileName.endsWith(".rm")
                    && !fileName.endsWith(".asf")
                    && !fileName.endsWith(".avi")

                    // unnecessary system files
                    && !fileName.endsWith("thumbs.db")
                    && !fileName.endsWith(".ds_store")
            ) {
                System.out.println("\t -> File \"" + f + "\" is not a MP3 file (size=" + f.toFile().length() + ")");
            }
        }
        return isAudioFile;
    }

    public FileObject asFileObject(FileInformation fileInformation) {
        var fo = new FileObject();
        fo.setLastChanged(Instant.ofEpochMilli(fileInformation.lastChanged()));
        fo.setFilename(fileInformation.fileName());
        fo.setPath(fileInformation.path());
        fo.setFileSize(fileInformation.fileSize());
        fo.setHash(fileInformation.hash());
        fo.setHashChecked(Instant.now());
        fo.setMetadata(fileInformation.metadata());

        var metadata = fileInformation.metadata();
        if (metadata.isEmpty()) {
            return fo;
        }
        var formatMetadata = (Map<String, Object>) metadata.get("format");
        fo.setFormatName(asString(formatMetadata.get("format_name")));
        fo.setFormatLongName(asString(formatMetadata.get("format_long_name")));
        fo.setBitRate(asInteger(formatMetadata.get("bit_rate")));
        fo.setDuration(asDouble(formatMetadata.get("duration")));
        fo.setNbStreams(asDouble(formatMetadata.get("nb_streams")));
        fo.setNbPrograms(asDouble(formatMetadata.get("nb_programs")));
        fo.setProbeScore(asDouble(formatMetadata.get("probe_score")));
        fo.setNbStreamGroups(asDouble(formatMetadata.get("nb_stream_groups")));
        fo.setStartTime(asDouble(formatMetadata.get("start_time")));

        Map<String, Object> tags = getFormatFromMap(metadata);
        if (tags != null && !tags.isEmpty()) {
            fo.setArtist(asString(tags.get("artist")));
            fo.setTitle(asString(tags.get("title")));
            fo.setTrack(asString(tags.get("track")));
            fo.setGenre(asString(tags.get("genre")));
            fo.setDate(asString(tags.get("date")));
            fo.setAlbum(asString(tags.get("album")));
            fo.setComment(asString(tags.get("comment")));
            fo.setDiscnumber(asString(tags.get("disc")));
            fo.setBpm(asFloat(tags.get("bpm")));
        }

        return fo;
    }

    private Map<String, Object> getFormatFromMap(Map<String, Object> metadata) {
        var formatMetadataObject = metadata.get("format");
        if (formatMetadataObject instanceof Map formatMetadata && formatMetadata.containsKey("tags")) {
            return (Map<String, Object>) formatMetadata.get("tags");
        }
        List<?> listOfStreams = (List<?>) metadata.get("streams");
        return listOfStreams.stream().map(entry -> (Map<String, Object>) entry)
                .filter(p -> p.containsKey("tags"))
                .map(p -> p.get("tags"))
                .map(p -> (Map<String, Object>) p)
                .map(p -> {
                    Map<String, Object> result = new HashMap<>();
                    // add lowercase keys
                    for (String keysOfMap : p.keySet()) {
                        result.put(keysOfMap.toLowerCase(), p.get(keysOfMap));
                        result.put(keysOfMap, p.get(keysOfMap));
                    }
                    return result;
                })
                .findFirst().orElse(Map.of());
    }

    public String hashOfFileObject(FileObject p) throws IOException {
        return _hashService.hash(Path.of(p.getPath(), p.getFilename()));
    }

    public boolean exists(FileObject fileObject) {
        return Files.exists(Path.of(fileObject.getPath(), fileObject.getFilename()));
    }
}
