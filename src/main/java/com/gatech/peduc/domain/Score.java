package com.gatech.peduc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Score.
 */
@Entity
@Table(name = "score")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "score")
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "excellent")
    private Integer excellent;

    @Column(name = "very_good")
    private Integer veryGood;

    @Column(name = "fair")
    private Integer fair;

    @Column(name = "bad")
    private Integer bad;

    @OneToOne(mappedBy = "score")
    @JsonIgnore
    private Peer peer;

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

    public Score excellent(Integer excellent) {
        this.excellent = excellent;
        return this;
    }

    public void setExcellent(Integer excellent) {
        this.excellent = excellent;
    }

    public Integer getVeryGood() {
        return veryGood;
    }

    public Score veryGood(Integer veryGood) {
        this.veryGood = veryGood;
        return this;
    }

    public void setVeryGood(Integer veryGood) {
        this.veryGood = veryGood;
    }

    public Integer getFair() {
        return fair;
    }

    public Score fair(Integer fair) {
        this.fair = fair;
        return this;
    }

    public void setFair(Integer fair) {
        this.fair = fair;
    }

    public Integer getBad() {
        return bad;
    }

    public Score bad(Integer bad) {
        this.bad = bad;
        return this;
    }

    public void setBad(Integer bad) {
        this.bad = bad;
    }

    public Peer getPeer() {
        return peer;
    }

    public Score peer(Peer peer) {
        this.peer = peer;
        return this;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
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
        Score score = (Score) o;
        if (score.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), score.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Score{" +
            "id=" + getId() +
            ", excellent=" + getExcellent() +
            ", veryGood=" + getVeryGood() +
            ", fair=" + getFair() +
            ", bad=" + getBad() +
            "}";
    }
}
