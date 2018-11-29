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
 * A SuggestedLecture.
 */
@Entity
@Table(name = "suggested_lecture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "suggestedlecture")
public class SuggestedLecture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @Column(name = "profile_picture_content_type")
    private String profilePictureContentType;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "average_score")
    private Integer averageScore;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public SuggestedLecture profilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePictureContentType() {
        return profilePictureContentType;
    }

    public SuggestedLecture profilePictureContentType(String profilePictureContentType) {
        this.profilePictureContentType = profilePictureContentType;
        return this;
    }

    public void setProfilePictureContentType(String profilePictureContentType) {
        this.profilePictureContentType = profilePictureContentType;
    }

    public String getFirstName() {
        return firstName;
    }

    public SuggestedLecture firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SuggestedLecture lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public SuggestedLecture averageScore(Integer averageScore) {
        this.averageScore = averageScore;
        return this;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }

    public String getCategory() {
        return category;
    }

    public SuggestedLecture category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public SuggestedLecture title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return duration;
    }

    public SuggestedLecture duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public SuggestedLecture language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVideoCallLink() {
        return videoCallLink;
    }

    public SuggestedLecture videoCallLink(String videoCallLink) {
        this.videoCallLink = videoCallLink;
        return this;
    }

    public void setVideoCallLink(String videoCallLink) {
        this.videoCallLink = videoCallLink;
    }

    public ZonedDateTime getPresentationDate() {
        return presentationDate;
    }

    public SuggestedLecture presentationDate(ZonedDateTime presentationDate) {
        this.presentationDate = presentationDate;
        return this;
    }

    public void setPresentationDate(ZonedDateTime presentationDate) {
        this.presentationDate = presentationDate;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public SuggestedLecture timeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Instant getPublicationDate() {
        return publicationDate;
    }

    public SuggestedLecture publicationDate(Instant publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public void setPublicationDate(Instant publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Peer getPeer() {
        return peer;
    }

    public SuggestedLecture peer(Peer peer) {
        this.peer = peer;
        return this;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public User getUser() {
        return user;
    }

    public SuggestedLecture user(User user) {
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
        SuggestedLecture suggestedLecture = (SuggestedLecture) o;
        if (suggestedLecture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), suggestedLecture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SuggestedLecture{" +
            "id=" + getId() +
            ", profilePicture='" + getProfilePicture() + "'" +
            ", profilePictureContentType='" + getProfilePictureContentType() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", averageScore=" + getAverageScore() +
            ", category='" + getCategory() + "'" +
            ", title='" + getTitle() + "'" +
            ", duration=" + getDuration() +
            ", language='" + getLanguage() + "'" +
            ", videoCallLink='" + getVideoCallLink() + "'" +
            ", presentationDate='" + getPresentationDate() + "'" +
            ", timeZone='" + getTimeZone() + "'" +
            ", publicationDate='" + getPublicationDate() + "'" +
            "}";
    }
}
