package de.jmizv.musicvault.domain.lastfm.model;

import com.google.gson.annotations.SerializedName;

public class LastFmAlbum {
  private String mbid;
  @SerializedName("#text")
  private String name;

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

  @Override
  public String toString() {
    return "LastFmAlbum{" +
           "name='" + name + '\'' +
           '}';
  }
}
