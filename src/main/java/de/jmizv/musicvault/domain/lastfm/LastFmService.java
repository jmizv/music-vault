package de.jmizv.musicvault.domain.lastfm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.jmizv.musicvault.domain.lastfm.json.DateTypeAdapter;
import de.jmizv.musicvault.domain.lastfm.model.LastFmRecentTracksResponse;
import de.jmizv.musicvault.domain.lastfm.model.LastFmTrack;
import me.tongfei.progressbar.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class LastFmService {

  private static final String URL = "http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks" +
                                    "&user={user}" +
                                    "&api_key={api-key}" +
                                    "&format=json" +
                                    "&limit=200" +
                                    "&from={from}" +
                                    "&to={to}" +
                                    "&page={page}" +
                                    "&extended=1";
  private static final Logger log = LoggerFactory.getLogger(LastFmService.class);

  private final HttpClient _client;
  private final String _lastFmApiKey;
  private final Gson _gson;

  public LastFmService(@Value("${lastfm.api-key}") String lastFmApiKey) {
    _client = HttpClient.newBuilder().build();
    _lastFmApiKey = lastFmApiKey;
    _gson = new GsonBuilder()
        .registerTypeAdapter(long.class, new DateTypeAdapter())
        .setPrettyPrinting()
        .create();
  }

  public void collect(String user, long startDateUtcEpochSeconds, long endDateUtcEpochSeconds, Consumer<List<LastFmTrack>> consumer) {
    int page = 1;
    try (ProgressBar bar = ProgressBar.builder().setInitialMax(10_000).setTaskName("Collect scrobbles").build()) {
      while (true) {
        if (!collect(user, startDateUtcEpochSeconds, endDateUtcEpochSeconds, page++, consumer, bar)) {
          break;
        }
      }
    }
  }

  private boolean collect(String user,
                          long startDateUtcEpochSeconds,
                          long endDateUtcEpochSeconds,
                          int page,
                          Consumer<List<LastFmTrack>> trackConsumer,
                          ProgressBar bar) {
    var uri = new UriTemplate(URL).expand(
        Map.of("user", user,
            "api-key", _lastFmApiKey,
            "from", startDateUtcEpochSeconds,
            "to", endDateUtcEpochSeconds,
            "page", page));
    var getRequest = HttpRequest.newBuilder(uri)
        .GET()
        .build();
    try {
      HttpResponse<String> response = _client.send(getRequest, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() / 100 > 2) {
        var headers = response.headers().map();
        log.error("Headers:\n{}\nBody:\n{}",
            headers.entrySet().stream().map(k -> k.getKey() + ": [" + String.join(",", k.getValue()) + "]").collect(Collectors.joining("\n")),
            response.body() != null ? response.body() : "<empty body>");
        throw new IllegalStateException("Last.fm API returned status code " + response.statusCode());
      }
      var recentTrackResponse = toListOfTracks(response.body());
      bar.maxHint(recentTrackResponse.paginationInfo().totalPages());
      bar.step();
      trackConsumer.accept(recentTrackResponse.recentTracks());
      return recentTrackResponse.paginationInfo().page() < recentTrackResponse.paginationInfo().totalPages();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  LastFmRecentTracksResponse toListOfTracks(String response) {
    return _gson.fromJson(response, LastFmRecentTracksResponse.class);
  }
}
