DROP TYPE IF EXISTS musicbrainz_type;

CREATE TYPE musicbrainz_type AS ENUM (
'Artist', 'Album', 'Release', 'Track'
);

-- ###

DROP TABLE IF EXISTS vault_user;
DROP TABLE IF EXISTS file_object;
DROP TABLE IF EXISTS musicbrainz_object;
DROP TABLE IF EXISTS scrobble;

CREATE TABLE vault_user(
  id serial PRIMARY KEY,
  name VARCHAR(255) not null,
  -- The last.fm user name to which the songs are scrobbled
  lastfm_username VARCHAR(255),
  -- The discogs user name
  discogs_username VARCHAR(255)
);

CREATE TABLE file_object(
  id serial PRIMARY KEY,

  filename VARCHAR(255) NOT NULL,
  path_ VARCHAR(1024) NOT NULL,
  file_size INT NOT NULL,
  last_changed TIMESTAMP NOT NULL,
  hash VARCHAR(63) NOT NULL UNIQUE,
  hash_checked TIMESTAMP NOT NULL,

  metadata JSON,

  format_name VARCHAR(64),
  format_long_name VARCHAR(255),
  bit_rate INT,
  duration FLOAT,
  nb_streams FLOAT,
  nb_programs FLOAT,
  probe_score FLOAT,
  nb_stream_groups FLOAT,
  start_time FLOAT,

  artist VARCHAR(255),
  title VARCHAR(255),
  album VARCHAR(255),
  date_ VARCHAR(255),
  genre VARCHAR(255),
  track VARCHAR(255),
  discnumber VARCHAR(255),
  bpm FLOAT,
  comment_ VARCHAR(4000),

  constraint file_unique unique (path_, file)
);

CREATE TABLE scrobble (
  id serial PRIMARY KEY,
  artist VARCHAR(1024) NOT NULL,
  artist_mbid VARCHAR(36),
  title VARCHAR(1024) NOT NULL,
  mbid VARCHAR(36),
  album VARCHAR(1024),
  album_mbid VARCHAR(36),
  scrobble_date TIMESTAMP NOT NULL
);

CREATE musicbrainz_object(
  id serial PRIMARY KEY,
  mb_type musicbrainz_type NOT NULL,
  mb_id VARCHAR(36) NOT NULL
);

-- ###

CREATE INDEX file_object_path_filename
ON file_object(path_,filename);

CREATE INDEX file_object_artist
ON file_object(artist);
-- ###

-- ###

CREATE VIEW simple_file_object AS select
  id,
  filename,
  path_,
  file_size,
  last_changed,
  hash,
  hash_checked,
  format_name,
  format_long_name,
  bit_rate,
  duration,
  nb_streams,
  nb_programs,
  probe_score,
  nb_stream_groups,
  start_time,
  artist,
  title,
  album,
  date_,
  genre,
  track,
  discnumber,
  bpm,
  comment_
  FROM file_object ORDER BY id;