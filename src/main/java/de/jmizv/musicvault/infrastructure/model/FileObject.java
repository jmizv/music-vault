package de.jmizv.musicvault.infrastructure.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "file_object")
public class FileObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal id;
    private String filename;
    @Column(name = "path_")
    private String path;
    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "last_changed")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant lastChanged;
    private String hash;
    @Column(name = "hash_checked")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant hashChecked;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;

    @Column(name = "format_name")
    private String formatName;
    @Column(name = "format_long_name")
    private String formatLongName;
    @Column(name = "bit_rate")
    private Integer bitRate;
    private Double duration;
    @Column(name = "nb_streams")
    private Double nbStreams;
    @Column(name = "nb_programs")
    private Double nbPrograms;
    @Column(name = "probe_score")
    private Double probeScore;
    @Column(name = "nb_stream_groups")
    private Double nbStreamGroups;
    @Column(name = "start_time")
    private Double startTime;

    private String artist;
    private String title;
    private String album;
    @Column(name = "date_")
    private String date;
    private String genre;
    private String track;
    private String discnumber;
    private Float bpm;
    @Column(name = "comment_")
    private String comment;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Instant getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Instant lastChanged) {
        this.lastChanged = lastChanged;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public String getFormatLongName() {
        return formatLongName;
    }

    public void setFormatLongName(String formatLongName) {
        this.formatLongName = formatLongName;
    }

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getNbStreams() {
        return nbStreams;
    }

    public void setNbStreams(Double nbStreams) {
        this.nbStreams = nbStreams;
    }

    public Double getNbPrograms() {
        return nbPrograms;
    }

    public void setNbPrograms(Double nbPrograms) {
        this.nbPrograms = nbPrograms;
    }

    public Double getProbeScore() {
        return probeScore;
    }

    public void setProbeScore(Double probeScore) {
        this.probeScore = probeScore;
    }

    public Double getNbStreamGroups() {
        return nbStreamGroups;
    }

    public void setNbStreamGroups(Double nbStreamGroups) {
        this.nbStreamGroups = nbStreamGroups;
    }

    public Double getStartTime() {
        return startTime;
    }

    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getDiscnumber() {
        return discnumber;
    }

    public void setDiscnumber(String discnumber) {
        this.discnumber = discnumber;
    }

    public Float getBpm() {
        return bpm;
    }

    public void setBpm(Float bpm) {
        this.bpm = bpm;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getHashChecked() {
        return hashChecked;
    }

    public void setHashChecked(Instant hashChecked) {
        this.hashChecked = hashChecked;
    }
}
