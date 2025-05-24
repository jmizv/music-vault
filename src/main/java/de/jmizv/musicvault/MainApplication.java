package de.jmizv.musicvault;

import de.jmizv.musicvault.domain.lastfm.LastFmService;
import de.jmizv.musicvault.domain.lastfm.ScrobbleService;
import de.jmizv.musicvault.infrastructure.service.FileObjectService;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@SpringBootApplication
public class MainApplication implements CommandLineRunner {

  private final FileObjectService _fileObjectService;
  private final String _basePath;
  private final LastFmService _lastFmService;
  private final ScrobbleService _scrobbleService;

  public MainApplication(FileObjectService fileObjectService,
                         @Value("${base-path}") String basePath,
                         LastFmService lastFmService,
                         ScrobbleService scrobbleService) {
    _basePath = Path.of(basePath).toAbsolutePath().toString();
    _fileObjectService = fileObjectService;
    _lastFmService = lastFmService;
    _scrobbleService = scrobbleService;
  }

  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }

  @Override
  public void run(String... args) throws IOException, ParseException {
    var options = new Options();
    options.addOption(new Option("m", "missing", false, "Check for missing files and remove them from the database"));
    options.addOption(new Option("c", "consistency", false, "Check consistency of files"));
    options.addOption(new Option("n", "new", false, "Check for new files and add them to the database"));
    options.addOption(new Option("m3u", true, "Creates a M3U8-playlist with the shortest song of each band sort by length and writes it to the given argument"));
    options.addOption(new Option("h", "help", false, "Prints this help"));
    options.addOption(new Option("l", "lastfm", false, "Update lastfm scrobbled tracks"));

    CommandLineParser parser = new DefaultParser();
    CommandLine commandLine = parser.parse(options, args);

    if (commandLine.hasOption("h") || !commandLine.iterator().hasNext()) {
      var helpFormatter = new HelpFormatter();
      helpFormatter.printHelp("ant", options);
      return;
    }

    if (commandLine.hasOption("m")) {
      _fileObjectService.checkMissingFiles(_basePath);
    }
    if (commandLine.hasOption("c")) {
      _fileObjectService.checkConsistencyOfFiles(_basePath);
    }
    if (commandLine.hasOption("n")) {
      _fileObjectService.checkForNewFiles(_basePath);
    }
    if (commandLine.hasOption("m3u")) {
      var argument = commandLine.getOptionValue("m3u");
      try (var printWriter = new PrintWriter(new FileWriter(argument, StandardCharsets.UTF_8))) {
        _fileObjectService.writeM3uWithShortestTracksOfEachArtist(_basePath, printWriter);
        printWriter.flush();
      }
    }
    if (commandLine.hasOption("l")) {
      var instant = _scrobbleService.maxScrobbleDate();
      var start = Optional.ofNullable(instant)
          .map(i -> i.plus(1, ChronoUnit.SECONDS))
          .orElse(Instant.parse("2005-03-01T00:00:00Z"))
          .getEpochSecond();
      var end = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).toEpochSecond(ZoneOffset.UTC);
      _lastFmService.collect("RJ", start, end, l -> ScrobbleService.fromTracks(l).forEach(_scrobbleService::create));
    }
  }
}
