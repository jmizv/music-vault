package de.jmizv.musicvault.domain.lastfm.model;

import java.util.List;

public class LastFmArtist {

  private String url;
  private String name;
  private List<LastFmImage> image;
  private String mbid;

  public String url() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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

  public String mbid() {
    return mbid;
  }

  public void setMbid(String mbid) {
    this.mbid = mbid;
  }

  @Override
  public String toString() {
    return "LastFmArtist{" +
           "name='" + name + '\'' +
           '}';
  }
}
