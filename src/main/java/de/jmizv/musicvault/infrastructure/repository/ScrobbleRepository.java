package de.jmizv.musicvault.infrastructure.repository;

import de.jmizv.musicvault.infrastructure.model.ScrobbleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.math.BigDecimal;
import java.time.Instant;

public interface ScrobbleRepository extends JpaRepository<ScrobbleEntity, BigDecimal> {

  @NativeQuery("SELECT max(scrobble_date) FROM scrobble")
  Instant findMaxScrobbleDate();

  @NativeQuery("SELECT min(scrobble_date) FROM scrobble")
  Instant findMinScrobbleDate();
}
