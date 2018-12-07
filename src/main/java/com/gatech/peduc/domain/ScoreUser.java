package com.gatech.peduc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ScoreUser.
 */
@Entity
@Table(name = "score_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "scoreuser")
public class ScoreUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 0)
    @Max(value = 1000000)
    @Column(name = "excellent")
    private Integer excellent;

    @Column(name = "very_good")
    private Integer veryGood;

    @Column(name = "fair")
    private Integer fair;

    @Column(name = "bad")
    private Integer bad;

    @Column(name = "average")
    private Integer average;

    @Column(name = "description")
    private String description;

    @OneToOne    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Lecture lecture;

    @ManyToOne
    @JsonIgnoreProperties("")
    private LectureActivity lectureActivity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExcellent() {
        return excellent;
    }

    public ScoreUser excellent(Integer excellent) {
        this.excellent = excellent;
        return this;
    }

    public void setExcellent(Integer excellent) {
        this.excellent = excellent;
    }

    public Integer getVeryGood() {
        return veryGood;
    }

    public ScoreUser veryGood(Integer veryGood) {
        this.veryGood = veryGood;
        return this;
    }

    public void setVeryGood(Integer veryGood) {
        this.veryGood = veryGood;
    }

    public Integer getFair() {
        return fair;
    }

    public ScoreUser fair(Integer fair) {
        this.fair = fair;
        return this;
    }

    public void setFair(Integer fair) {
        this.fair = fair;
    }

    public Integer getBad() {
        return bad;
    }

    public ScoreUser bad(Integer bad) {
        this.bad = bad;
        return this;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public Integer getAverage() {
        return average;
    }

    public ScoreUser average(Integer average) {
        this.average = average;
        return this;
    }

    public void setAverage(Integer average) {
        this.average = average;
    }

    public String getDescription() {
        return description;
    }

    public ScoreUser description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public ScoreUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public ScoreUser lecture(Lecture lecture) {
        this.lecture = lecture;
        return this;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public LectureActivity getLectureActivity() {
        return lectureActivity;
    }

    public ScoreUser lectureActivity(LectureActivity lectureActivity) {
        this.lectureActivity = lectureActivity;
        return this;
    }

    public void setLectureActivity(LectureActivity lectureActivity) {
        this.lectureActivity = lectureActivity;
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
        ScoreUser scoreUser = (ScoreUser) o;
        if (scoreUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scoreUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ScoreUser{" +
            "id=" + getId() +
            ", excellent=" + getExcellent() +
            ", veryGood=" + getVeryGood() +
            ", fair=" + getFair() +
            ", bad=" + getBad() +
            ", average=" + getAverage() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
