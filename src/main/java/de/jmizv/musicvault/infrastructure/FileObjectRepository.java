package de.jmizv.musicvault.infrastructure;

import de.jmizv.musicvault.infrastructure.model.FileObject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FileObjectRepository extends CrudRepository<FileObject, BigDecimal> {

    Optional<FileObject> findByFilenameAndPath(String filename, String path);

    @Query(value = "SELECT fo FROM FileObject fo WHERE path LIKE concat(?1,'%')")
    List<FileObject> findAll(String path);

    FileObject findByHash(String hash);
}
