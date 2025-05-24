package de.jmizv.musicvault.domain.lastfm.model;

public class LastFmRecentTracksPaginationInfo {

  private int perPage;
  private int totalPages;
  private int page;
  private String user;
  private int total;

  public int perPage() {
    return perPage;
  }

  public int totalPages() {
    return totalPages;
  }

  public int page() {
    return page;
  }

  public String user() {
    return user;
  }

  public int total() {
    return total;
  }
}
