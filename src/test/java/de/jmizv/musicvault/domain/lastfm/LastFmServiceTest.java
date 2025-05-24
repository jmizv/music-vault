package de.jmizv.musicvault.domain.lastfm;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LastFmServiceTest {

  private LastFmService _lastFmService;

  @BeforeEach
  void beforeEach() {
    _lastFmService = new LastFmService("invalid-api-key");
  }

  @Test
  void should_parse_json_response_to_list_of_tracks() {
    var response = """
        {
        	"recenttracks": {
        		"track": [
        			{
        				"artist": {
        					"url": "https://www.last.fm/music/Meduza",
        					"name": "Meduza",
        					"image": [
        						{
        							"size": "small",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
        						},
        						{
        							"size": "medium",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
        						},
        						{
        							"size": "large",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
        						},
        						{
        							"size": "extralarge",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
        						}
        					],
        					"mbid": ""
        				},
        				"mbid": "",
        				"name": "Always There",
        				"image": [
        					{
        						"size": "small",
        						"#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
        					},
        					{
        						"size": "medium",
        						"#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
        					},
        					{
        						"size": "large",
        						"#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
        					},
        					{
        						"size": "extralarge",
        						"#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
        					}
        				],
        				"streamable": "0",
        				"album": {
        					"mbid": "",
        					"#text": "Life is Change Volume 3"
        				},
        				"url": "https://www.last.fm/music/Meduza/_/Always+There",
        				"@attr": {
        					"nowplaying": "true"
        				},
        				"loved": "0"
        			},
        			{
        				"artist": {
        					"url": "https://www.last.fm/music/Knochenfabrik",
        					"name": "Knochenfabrik",
        					"image": [
        						{
        							"size": "small",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
        						},
        						{
        							"size": "medium",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
        						},
        						{
        							"size": "large",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
        						},
        						{
        							"size": "extralarge",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
        						}
        					],
        					"mbid": ""
        				},
        				"date": {
        					"uts": "1117411100",
        					"#text": "29 May 2005, 23:58"
        				},
        				"mbid": "3839e07f-7bd8-419f-8513-6adbb6cc7f00",
        				"name": "Filmriss",
        				"image": [
        					{
        						"size": "small",
        						"#text": ""
        					},
        					{
        						"size": "medium",
        						"#text": ""
        					},
        					{
        						"size": "large",
        						"#text": ""
        					},
        					{
        						"size": "extralarge",
        						"#text": ""
        					}
        				],
        				"url": "https://www.last.fm/music/Knochenfabrik/_/Filmriss",
        				"streamable": "0",
        				"album": {
        					"mbid": "",
        					"#text": ""
        				},
        				"loved": "0"
        			},
        			{
        				"artist": {
        					"url": "https://www.last.fm/music/Knochenfabrik",
        					"name": "Knochenfabrik",
        					"image": [
        						{
        							"size": "small",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
        						},
        						{
        							"size": "medium",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
        						},
        						{
        							"size": "large",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
        						},
        						{
        							"size": "extralarge",
        							"#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
        						}
        					],
        					"mbid": ""
        				},
        				"date": {
        					"uts": "1117410940",
        					"#text": "29 May 2005, 23:55"
        				},
        				"mbid": "1a17d2ba-d058-473d-b6b5-deda85afce12",
        				"name": "Grüne Haare",
        				"image": [
        					{
        						"size": "small",
        						"#text": ""
        					},
        					{
        						"size": "medium",
        						"#text": ""
        					},
        					{
        						"size": "large",
        						"#text": ""
        					},
        					{
        						"size": "extralarge",
        						"#text": ""
        					}
        				],
        				"url": "https://www.last.fm/music/Knochenfabrik/_/Gr%C3%BCne+Haare",
        				"streamable": "0",
        				"album": {
        					"mbid": "",
        					"#text": "Ameisenstaat"
        				},
        				"loved": "0"
        			}
        		],
        		"@attr": {
        			"perPage": "200",
        			"totalPages": "32",
        			"page": "1",
        			"user": "username",
        			"total": "6362"
        		}
        	}
        }
        """;
    var result = _lastFmService.toListOfTracks(response).recentTracks();
    assertThat(result).hasSize(3);
    assertThat(result.getFirst()).satisfies(track -> {
      assertThat(track.date()).isZero();
      assertThat(track.name()).isEqualTo("Always There");
      assertThat(track.artist().name()).isEqualTo("Meduza");
      assertThat(track.album().name()).isEqualTo("Life is Change Volume 3");
    });
    assertThat(result.get(1)).satisfies(track -> {
      assertThat(track.date()).isEqualTo(1117411100L);
      assertThat(track.name()).isEqualTo("Filmriss");
      assertThat(track.artist().name()).isEqualTo("Knochenfabrik");
      assertThat(track.album().name()).isEmpty();
    });
    assertThat(result.getLast()).satisfies(track -> {
      assertThat(track.date()).isEqualTo(1117410940L);
      assertThat(track.name()).isEqualTo("Grüne Haare");
      assertThat(track.mbid()).isEqualTo("1a17d2ba-d058-473d-b6b5-deda85afce12");
      assertThat(track.artist().name()).isEqualTo("Knochenfabrik");
      assertThat(track.album().name()).isEqualTo("Ameisenstaat");
    });
  }

}