package de.jmizv.musicvault.infrastructure.service;

import de.jmizv.musicvault.domain.file.FileService;
import de.jmizv.musicvault.infrastructure.model.FileObject;
import de.jmizv.musicvault.infrastructure.repository.FileObjectRepository;
import me.tongfei.progressbar.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

@Service
public class FileObjectService {

    private static final Logger log = LoggerFactory.getLogger(FileObjectService.class);

    private final FileObjectRepository _fileObjectRepository;
    private final FileService _fileService;

    public FileObjectService(FileObjectRepository fileObjectRepository, FileService fileService) {
        _fileObjectRepository = fileObjectRepository;
        _fileService = fileService;
    }

    public FileObject create(FileObject fileObject) {
        var comment = fileObject.getComment();
        if (comment != null && comment.length() > 4000) {
            comment = comment.replace("  ", " ").replace("\n\n", "\n");
            if (comment.length() > 4000) {
                log.error("Comment has a length of {} in file {}/{}", comment.length(), fileObject.getPath(), fileObject.getFilename());
                comment = comment.substring(0, 4000);
            }
            fileObject.setComment(comment);
        }
        return _fileObjectRepository.save(fileObject);
    }

    public Optional<FileObject> getByFileAndPath(String file, String path) {
        return _fileObjectRepository.findByFilenameAndPath(file, path);
    }

    public List<FileObject> list() {
        return (List<FileObject>) _fileObjectRepository.findAll();
    }

    public List<FileObject> list(String path) {
        return _fileObjectRepository.findAll(path);
    }

    public void update(FileObject p) {
        _fileObjectRepository.save(p);
    }

    public void delete(FileObject fileObject) {
        _fileObjectRepository.delete(fileObject);
    }

    public void writeM3uWithShortestTracksOfEachArtist(String path, PrintWriter pw) {
        record Item(String artist, String title, String path, String filename, double duration) {
        }
        Map<String, List<Item>> c = new TreeMap<>();
        _fileObjectRepository.findAll(path)
                .stream()
                .filter(o -> o.getDuration() != null)
                .filter(o -> o.getArtist() != null)
                .filter(o -> o.getDuration() >= 31)
                .map(o -> new Item(o.getArtist(), o.getTitle(), o.getPath(), o.getFilename(), Math.round(o.getDuration())))
                .forEach(p -> {
                    c.computeIfAbsent(p.artist.toLowerCase(), _ -> new ArrayList<>());
                    c.get(p.artist.toLowerCase()).add(p);
                });

        List<Item> items = new ArrayList<>();
        for (Map.Entry<String, List<Item>> entry : c.entrySet()) {
            entry.getValue().sort(Comparator.comparing(Item::duration));
            items.add(entry.getValue().getFirst());
        }
        items.sort(Comparator.comparing(Item::duration));
        pw.println("#EXTM3U");
        items.forEach(d -> pw.append(String.format("#EXTINF:%s,%s - %s%n%s/%s%n",
                (int) d.duration(),
                d.artist(),
                d.title(),
                d.path(),
                d.filename())));
    }

    public void checkMissingFiles(String basePath) {
        var fileObjects = list(basePath);
        int size = fileObjects.size();
        if (size == 0) {
            return;
        }
        try (ProgressBar pb = new ProgressBar("checkMissingFiles", size)) {
            fileObjects.stream().filter(fileObject -> {
                if (_fileService.exists(fileObject)) {
                    pb.step();
                    return false;
                }
                return true;
            }).forEach(fileObject -> {
                pb.step();
                delete(fileObject);
            });
        }
    }

    public void checkForNewFiles(String basePath) throws IOException {
        Path path = Path.of(basePath);
        var files = _fileService.countFiles(path);
        StringBuilder sb = new StringBuilder();
        try (ProgressBar pb = new ProgressBar("checkForNewFiles", files)) {
            _fileService.listFiles(path).filter(p -> {
                        if (getByFileAndPath(p.toFile().getName(), p.toFile().getParent()).isPresent()) {
                            pb.step();
                            return false;
                        }
                        return true;
                    })
                    .map(_fileService::fileInformation)
                    .map(_fileService::asFileObject)
                    .forEach(fos -> {
                        pb.step();
                        try {
                            create(fos);
                        } catch (Exception ex) {
                            if (ex.getMessage().contains("hash_unique")) {
                                var existing = _fileObjectRepository.findByHash(fos.getHash());
                                sb.append("%s\n%s\n\n".formatted(
                                        fos.fullPath(),
                                        existing.fullPath()));
                            } else {
                                log.error("Could not create file: {}", fos.fullPath(), ex);
                            }
                        }
                    });
            if (!sb.isEmpty()) {
                System.out.println("The following files are duplicates as their hash is equal:\n" + sb);
            }
        }
    }

    public void checkConsistencyOfFiles(String basePath) {
        var fileObjects = list();
        int[] maxSize = new int[]{fileObjects.size()};
        try (ProgressBar pb = new ProgressBar("checkConsistencyOfFiles", maxSize[0])) {
            fileObjects.stream().filter(p -> {
                if (!_fileService.exists(p)) {
                    pb.maxHint(--maxSize[0]);
                    return false;
                }
                return true;
            }).forEach(p -> {
                pb.step();
                try {
                    var hash = _fileService.hashOfFileObject(p);
                    if (!hash.equals(p.getHash())) {
                        log.warn("Hash for file {}/{} has changed.", p.getPath(), p.getFilename());
                    }
                } catch (IOException ex) {
                    log.error("Could not compute hash of file {}/{}", p.getPath(), p.getFilename(), ex);
                }
            });
        }
    }
}
