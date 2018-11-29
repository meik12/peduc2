package com.gatech.peduc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Lecture.
 */
@Entity
@Table(name = "lecture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lecture")
public class Lecture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @NotNull
    @Column(name = "topic", nullable = false)
    private String topic;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "key_word", nullable = false)
    private String keyWord;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "video_call_link")
    private String videoCallLink;

    @NotNull
    @Column(name = "presentation_date", nullable = false)
    private ZonedDateTime presentationDate;

    @Column(name = "time_zone")
    private String timeZone;

    @Column(name = "publication_date")
    private Instant publicationDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Peer peer;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public Lecture category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public Lecture topic(String topic) {
        this.topic = topic;
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public Lecture title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public Lecture keyWord(String keyWord) {
        this.keyWord = keyWord;
        return this;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Integer getDuration() {
        return duration;
    }

    public Lecture duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public Lecture status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public Lecture language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVideoCallLink() {
        return videoCallLink;
    }

    public Lecture videoCallLink(String videoCallLink) {
        this.videoCallLink = videoCallLink;
        return this;
    }

    public void setVideoCallLink(String videoCallLink) {
        this.videoCallLink = videoCallLink;
    }

    public ZonedDateTime getPresentationDate() {
        return presentationDate;
    }

    public Lecture presentationDate(ZonedDateTime presentationDate) {
        this.presentationDate = presentationDate;
        return this;
    }

    public void setPresentationDate(ZonedDateTime presentationDate) {
        this.presentationDate = presentationDate;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public Lecture timeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Instant getPublicationDate() {
        return publicationDate;
    }

    public Lecture publicationDate(Instant publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public void setPublicationDate(Instant publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Peer getPeer() {
        return peer;
    }

    public Lecture peer(Peer peer) {
        this.peer = peer;
        return this;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public User getUser() {
        return user;
    }

    public Lecture user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lecture lecture = (Lecture) o;
        if (lecture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lecture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Lecture{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", topic='" + getTopic() + "'" +
            ", title='" + getTitle() + "'" +
            ", keyWord='" + getKeyWord() + "'" +
            ", duration=" + getDuration() +
            ", status='" + getStatus() + "'" +
            ", language='" + getLanguage() + "'" +
            ", videoCallLink='" + getVideoCallLink() + "'" +
            ", presentationDate='" + getPresentationDate() + "'" +
            ", timeZone='" + getTimeZone() + "'" +
            ", publicationDate='" + getPublicationDate() + "'" +
            "}";
    }
}
