# Music Vault

Combine all your music: online/offline/global/local/physical/virtual:

* [discogs.com](https://discogs.com)
* [last.fm](https://www.last.fm)
* local MP3 collection

Questions this will answer:

* Do I have the vinyls for my most listened songs?
* Do I have scrobbled my MP3 files at least once?
* Are there any digital copies missing for some artists?

## Details

### Configuration

Define a data source for the application:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/musicvault
spring.datasource.username=
spring.datasource.password=
```

Define where the application can find the `ffprobe` executable which is needed to extract the metadata from 
the music files:

`ffprobe.path-to-executable=`

Define the base path, i.e. where all the music files are located:

`base-path=C:\\Music`

Currently, it is not supported to have more than one base folder.

### Database

This project works with a PostgreSQL database. Find the `src/main/resources/datamodel.sql` for the datamodel used. Note
that the data model is not applied automatically at first start of the app.

### App

Starting the Spring Boot app will
* check for missing files, i.e. files that are found in the database but not in the file system. Such files are removed from the database
* check consistency of the files, i.e. if the stored hash value is still the same
* check for new files, i.e. files that are found in the file system but not in the database. Such files will be added to the database with all there metadata
* generate a M3U8 file with the shortest song per artist ordered by length. Only songs with at least 31 seconds are considered.

## Notes

* ffprobe works with: `ffprobe -v quiet -print_format json -show_format -show_streams ${file}`
* ffprobe supports
    * MP3
    * WMA
    * M4A
    * OGG
    * FLAC

We can remove certain tags from a file, for example `id3v2_priv.http://wwww.cdtag.com`:

```shell
ffmpeg -i input -c copy -map_metadata 0 -metadata 'id3v2_priv.http://www.cdtag.com=' output.mp3
```

Note the equal sign at the end of the tag name.