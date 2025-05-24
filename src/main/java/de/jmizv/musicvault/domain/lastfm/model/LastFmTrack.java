package de.jmizv.musicvault.domain.lastfm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class LastFmTrack {
  private LastFmArtist artist;
  private long date;
  private String mbid;
  private String name;
  private List<LastFmImage> image;
  private boolean streamable;
  private LastFmAlbum album;
  private String url;
  @SerializedName("@attr")
  private Map<String, String> nowPlayingAttributeMap;

  public LastFmArtist artist() {
    return artist;
  }

  public void setArtist(LastFmArtist artist) {
    this.artist = artist;
  }

  public long date() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public String mbid() {
    return mbid;
  }

  public void setMbid(String mbid) {
    this.mbid = mbid;
  }

  public String name() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<LastFmImage> image() {
    return image;
  }

  public void setImage(List<LastFmImage> image) {
    this.image = image;
  }

  public boolean streamable() {
    return streamable;
  }

  public void setStreamable(boolean streamable) {
    this.streamable = streamable;
  }

  public LastFmAlbum album() {
    return album;
  }

  public void setAlbum(LastFmAlbum album) {
    this.album = album;
  }

  public String url() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "LastFmTrack{" +
           "name='" + name + '\'' +
           ", artist=" + artist +
           ", date=" + date +
           '}';
  }

  public boolean isNowPlaying() {
    return nowPlayingAttributeMap != null
           && nowPlayingAttributeMap.get("nowplaying") != null
           && String.valueOf(nowPlayingAttributeMap.get("nowplaying")).equalsIgnoreCase("true");
  }
}
