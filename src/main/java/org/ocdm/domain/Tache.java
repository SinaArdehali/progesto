package org.ocdm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Tache.
 */
@Entity
@Table(name = "tache")
public class Tache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom_tache")
    private String nomTache;

    @Column(name = "description_tache")
    private String descriptionTache;

    @Column(name = "debut_tache")
    private LocalDate debutTache;

    @Column(name = "fin_tache")
    private LocalDate finTache;

    @Column(name = "nb_quart_jour_attribuer")
    private Integer nbQuartJourAttribuer;

    @Column(name = "jours_vendus_tache")
    private Float joursVendusTache;

    @OneToMany(mappedBy = "tacheMere", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<AttributionTache> tacheEmployes = new HashSet<>();

    @ManyToOne
    private Projet projetMere;


    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomTache() {
        return nomTache;
    }

    public Tache nomTache(String nomTache) {
        this.nomTache = nomTache;
        return this;
    }

    public void setNomTache(String nomTache) {
        this.nomTache = nomTache;
    }

    public String getDescriptionTache() {
        return descriptionTache;
    }

    public Tache descriptionTache(String descriptionTache) {
        this.descriptionTache = descriptionTache;
        return this;
    }

    public void setDescriptionTache(String descriptionTache) {
        this.descriptionTache = descriptionTache;
    }

    public LocalDate getDebutTache() {
        return debutTache;
    }

    public Tache debutTache(LocalDate debutTache) {
        this.debutTache = debutTache;
        return this;
    }

    public void setDebutTache(LocalDate debutTache) {
        this.debutTache = debutTache;
    }

    public LocalDate getFinTache() {
        return finTache;
    }

    public Tache finTache(LocalDate finTache) {
        this.finTache = finTache;
        return this;
    }

    public void setFinTache(LocalDate finTache) {
        this.finTache = finTache;
    }

    public Integer getNbQuartJourAttribuer() {
        return nbQuartJourAttribuer;
    }

    public Tache nbQuartJourAttribuer(Integer nbQuartJourAttribuer) {
        this.nbQuartJourAttribuer = nbQuartJourAttribuer;
        return this;
    }

    public void setNbQuartJourAttribuer(Integer nbQuartJourAttribuer) {
        this.nbQuartJourAttribuer = nbQuartJourAttribuer;
    }

    public Float getJoursVendusTache() {
        return joursVendusTache;
    }

    public Tache joursVendusTache(Float joursVendusTache) {
        this.joursVendusTache = joursVendusTache;
        return this;
    }

    public void setJoursVendusTache(Float joursVendusTache) {
        this.joursVendusTache = joursVendusTache;
    }

    public Set<AttributionTache> getTacheEmployes() {
        return tacheEmployes;
    }

    public Tache tacheEmployes(Set<AttributionTache> attributionTaches) {
        this.tacheEmployes = attributionTaches;
        return this;
    }

    public Tache addTacheEmploye(AttributionTache attributionTache) {
        this.tacheEmployes.add(attributionTache);
        attributionTache.setTacheMere(this);
        return this;
    }

    public Tache removeTacheEmploye(AttributionTache attributionTache) {
        this.tacheEmployes.remove(attributionTache);
        attributionTache.setTacheMere(null);
        return this;
    }

    public void setTacheEmployes(Set<AttributionTache> attributionTaches) {
        this.tacheEmployes = attributionTaches;
    }

    public Projet getProjetMere() {
        return projetMere;
    }

    public Tache projetMere(Projet projet) {
        this.projetMere = projet;
        return this;
    }

    public void setProjetMere(Projet projet) {
        this.projetMere = projet;
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
        Tache tache = (Tache) o;
        if (tache.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tache.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tache{" +
            "id=" + getId() +
            ", nomTache='" + getNomTache() + "'" +
            ", descriptionTache='" + getDescriptionTache() + "'" +
            ", debutTache='" + getDebutTache() + "'" +
            ", finTache='" + getFinTache() + "'" +
            ", nbQuartJourAttribuer='" + getNbQuartJourAttribuer() + "'" +
            ", joursVendusTache='" + getJoursVendusTache() + "'" +
            "}";
    }
}
