package de.jmizv.musicvault.domain.lastfm.model;

import java.math.BigDecimal;

public class Scrobble {
  private BigDecimal id;
  private String artist;
  private String artistMbid;
  private String title;
  private String mbid;
  private String album;
  private String albumMbid;
  private long scrobbleDate;

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

  public long scrobbleDate() {
    return scrobbleDate;
  }

  public void setScrobbleDate(long scrobbleDate) {
    this.scrobbleDate = scrobbleDate;
  }

  @Override
  public String toString() {
    return "Scrobble{" +
           "artist='" + artist + '\'' +
           ", title='" + title + '\'' +
           '}';
  }
}
