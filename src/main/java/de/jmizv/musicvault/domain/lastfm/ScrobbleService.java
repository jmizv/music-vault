package de.jmizv.musicvault.domain.lastfm;

import de.jmizv.musicvault.domain.lastfm.model.LastFmTrack;
import de.jmizv.musicvault.domain.lastfm.model.Scrobble;
import de.jmizv.musicvault.infrastructure.model.ScrobbleEntity;
import de.jmizv.musicvault.infrastructure.repository.ScrobbleRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ScrobbleService {

  private final ScrobbleRepository _scrobbleRepository;

  public ScrobbleService(ScrobbleRepository scrobbleRepository) {
    _scrobbleRepository = scrobbleRepository;
  }

  public Scrobble create(Scrobble scrobble) {
    return toDomain(_scrobbleRepository.save(fromDomain(scrobble)));
  }

  public List<Scrobble> create(List<Scrobble> scrobbles){
    return _scrobbleRepository.saveAll(fromDomain(scrobbles)).stream().map(ScrobbleService::toDomain).toList();
  }

  public Instant maxScrobbleDate() {
    return _scrobbleRepository.findMaxScrobbleDate();
  }

  public Instant minScrobbleDate() {
    return _scrobbleRepository.findMinScrobbleDate();
  }

  public static ScrobbleEntity fromDomain(Scrobble scrobble) {
    var scrobbleEntity = new ScrobbleEntity();
    scrobbleEntity.setAlbum(scrobble.album());
    scrobbleEntity.setAlbumMbid(scrobble.albumMbid());
    scrobbleEntity.setArtist(scrobble.artist());
    scrobbleEntity.setArtistMbid(scrobble.artistMbid());
    scrobbleEntity.setTitle(scrobble.title());
    scrobbleEntity.setMbid(scrobble.mbid());
    scrobbleEntity.setScrobbleDate(Instant.ofEpochSecond(scrobble.scrobbleDate()));
    scrobbleEntity.setId(scrobble.id());
    return scrobbleEntity;
  }

  public static List<ScrobbleEntity> fromDomain(List<Scrobble> scrobbles) {
    return scrobbles.stream().map(ScrobbleService::fromDomain).toList();
  }

  public static Scrobble toDomain(ScrobbleEntity scrobbleEntity) {
    var scrobble = new Scrobble();
    scrobble.setMbid(scrobbleEntity.mbid());
    scrobble.setTitle(scrobbleEntity.title());
    scrobble.setScrobbleDate(scrobbleEntity.scrobbleDate().getEpochSecond());
    scrobble.setId(scrobbleEntity.id());
    scrobble.setAlbumMbid(scrobbleEntity.albumMbid());
    scrobble.setAlbum(scrobbleEntity.album());
    scrobble.setArtistMbid(scrobbleEntity.artistMbid());
    scrobble.setArtist(scrobbleEntity.artist());
    return scrobble;
  }

  public static List<Scrobble> fromTracks(List<LastFmTrack> tracks) {
    return tracks.stream().map(ScrobbleService::fromTrack).toList();
  }

  public static Scrobble fromTrack(LastFmTrack track) {
    var scrobble = new Scrobble();
    scrobble.setTitle(track.name());
    scrobble.setArtist(track.artist().name());
    scrobble.setArtistMbid(track.artist().mbid());
    if (track.album() != null) {
      scrobble.setAlbum(track.album().name());
      scrobble.setAlbumMbid(track.album().mbid());
    }
    scrobble.setScrobbleDate(track.date());
    scrobble.setMbid(track.mbid());
    return scrobble;
  }
}
