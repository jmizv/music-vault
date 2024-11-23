package de.jmizv.musicvault;

import de.jmizv.musicvault.infrastructure.FileObjectService;
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

@SpringBootApplication
public class MainApplication implements CommandLineRunner {

    private final FileObjectService _fileObjectService;
    private final String _basePath;

    public MainApplication(FileObjectService fileObjectService, @Value("${base-path}") String basePath) {
        _basePath = Path.of(basePath).toAbsolutePath().toString();
        _fileObjectService = fileObjectService;
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

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if (commandLine.hasOption("h")) {
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
            try (var pw = new PrintWriter(new FileWriter(argument, StandardCharsets.UTF_8))) {
                _fileObjectService.writeM3uWithShortestTracksOfEachArtist(_basePath, pw);
                pw.flush();
            }
        }
    }
}
