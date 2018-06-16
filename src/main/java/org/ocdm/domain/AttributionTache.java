package org.ocdm.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A AttributionTache.
 */
@Entity
@Table(name = "attribution_tache")
public class AttributionTache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_date")
    private LocalDate date;

    @Column(name = "quart_journee")
    private Integer quartJournee;

    @ManyToOne
    private User proprietaireTache;

    @ManyToOne
    private Tache tacheMere;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public AttributionTache date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getQuartJournee() {
        return quartJournee;
    }

    public AttributionTache quartJournee(Integer quartJournee) {
        this.quartJournee = quartJournee;
        return this;
    }

    public void setQuartJournee(Integer quartJournee) {
        this.quartJournee = quartJournee;
    }

    public User getProprietaireTache() {
        return proprietaireTache;
    }

    public AttributionTache proprietaireTache(User user) {
        this.proprietaireTache = user;
        return this;
    }

    public void setProprietaireTache(User user) {
        this.proprietaireTache = user;
    }

    public Tache getTacheMere() {
        return tacheMere;
    }

    public AttributionTache tacheMere(Tache tache) {
        this.tacheMere = tache;
        return this;
    }

    public void setTacheMere(Tache tache) {
        this.tacheMere = tache;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributionTache attributionTache = (AttributionTache) o;
        if (attributionTache.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attributionTache.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttributionTache{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", quartJournee='" + getQuartJournee() + "'" +
            "}";
    }

    public AttributionTache() {

    }

    public AttributionTache(Tache tacheMere) {
        this.tacheMere = tacheMere;
    }



}
