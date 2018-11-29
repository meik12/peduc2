package com.gatech.peduc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.gatech.peduc.domain.enumeration.LectureStatus;

/**
 * A LectureActivity.
 */
@Entity
@Table(name = "lecture_activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lectureactivity")
public class LectureActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "presenting_user_id")
    private Long presentingUserId;

    @Column(name = "atending_user_id")
    private Long atendingUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "lecture_status")
    private LectureStatus lectureStatus;

    @Column(name = "presentation_date")
    private ZonedDateTime presentationDate;

    @Column(name = "posted_date")
    private ZonedDateTime postedDate;

    @OneToOne    @JoinColumn(unique = true)
    private Lecture lecture;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "lecture_activity_user",
               joinColumns = @JoinColumn(name = "lecture_activities_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPresentingUserId() {
        return presentingUserId;
    }

    public LectureActivity presentingUserId(Long presentingUserId) {
        this.presentingUserId = presentingUserId;
        return this;
    }

    public void setPresentingUserId(Long presentingUserId) {
        this.presentingUserId = presentingUserId;
    }

    public Long getAtendingUserId() {
        return atendingUserId;
    }

    public LectureActivity atendingUserId(Long atendingUserId) {
        this.atendingUserId = atendingUserId;
        return this;
    }

    public void setAtendingUserId(Long atendingUserId) {
        this.atendingUserId = atendingUserId;
    }

    public LectureStatus getLectureStatus() {
        return lectureStatus;
    }

    public LectureActivity lectureStatus(LectureStatus lectureStatus) {
        this.lectureStatus = lectureStatus;
        return this;
    }

    public void setLectureStatus(LectureStatus lectureStatus) {
        this.lectureStatus = lectureStatus;
    }

    public ZonedDateTime getPresentationDate() {
        return presentationDate;
    }

    public LectureActivity presentationDate(ZonedDateTime presentationDate) {
        this.presentationDate = presentationDate;
        return this;
    }

    public void setPresentationDate(ZonedDateTime presentationDate) {
        this.presentationDate = presentationDate;
    }

    public ZonedDateTime getPostedDate() {
        return postedDate;
    }

    public LectureActivity postedDate(ZonedDateTime postedDate) {
        this.postedDate = postedDate;
        return this;
    }

    public void setPostedDate(ZonedDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public LectureActivity lecture(Lecture lecture) {
        this.lecture = lecture;
        return this;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public Set<User> getUsers() {
        return users;
    }

    public LectureActivity users(Set<User> users) {
        this.users = users;
        return this;
    }

    public LectureActivity addUser(User user) {
        this.users.add(user);
        return this;
    }

    public LectureActivity removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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
        LectureActivity lectureActivity = (LectureActivity) o;
        if (lectureActivity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lectureActivity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LectureActivity{" +
            "id=" + getId() +
            ", presentingUserId=" + getPresentingUserId() +
            ", atendingUserId=" + getAtendingUserId() +
            ", lectureStatus='" + getLectureStatus() + "'" +
            ", presentationDate='" + getPresentationDate() + "'" +
            ", postedDate='" + getPostedDate() + "'" +
            "}";
    }
}
