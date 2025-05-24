package de.jmizv.musicvault.domain.lastfm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.function.Predicate;

public class LastFmRecentTracksResponse {

  @SerializedName("recenttracks")
  private InnerResponse recentTracks;

  public List<LastFmTrack> recentTracks() {
    return recentTracks.track.stream().filter(Predicate.not(LastFmTrack::isNowPlaying)).toList();
  }

  public LastFmRecentTracksPaginationInfo paginationInfo() {
    return recentTracks._paginationInfo;
  }
}

class InnerResponse {
  List<LastFmTrack> track;
  @SerializedName("@attr")
  LastFmRecentTracksPaginationInfo _paginationInfo;
}
