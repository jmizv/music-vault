package de.jmizv.musicvault.infrastructure.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "scrobble")
public class ScrobbleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigDecimal id;
  @Column(name = "artist")
  private String artist;
  @Column(name = "artist_mbid")
  private String artistMbid;
  @Column(name = "title")
  private String title;
  @Column(name = "mbid")
  private String mbid;
  @Column(name = "album")
  private String album;
  @Column(name = "album_mbid")
  private String albumMbid;
  @Column(name = "scrobble_date")
  private Instant scrobbleDate;

  public BigDecimal id() {
    return id;
  }

  public void setId(BigDecimal id) {
    this.id = id;
  }

  public String artist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String artistMbid() {
    return artistMbid;
  }

  public void setArtistMbid(String artistMbid) {
    this.artistMbid = artistMbid;
  }

  public String title() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String mbid() {
    return mbid;
  }

  public void setMbid(String mbid) {
    this.mbid = mbid;
  }

  public String album() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public String albumMbid() {
    return albumMbid;
  }

  public void setAlbumMbid(String albumMbid) {
    this.albumMbid = albumMbid;
  }

  public Instant scrobbleDate() {
    return scrobbleDate;
  }

  public void setScrobbleDate(Instant scrobbleDate) {
    this.scrobbleDate = scrobbleDate;
  }

  @Override
  public String toString() {
    return "ScrobbleEntity{" +
           "artist='" + artist + '\'' +
           ", title='" + title + '\'' +
           ", scrobbleDate=" + scrobbleDate +
           '}';
  }
}
