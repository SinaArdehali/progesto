package org.ocdm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Projet.
 */
@Entity
@Table(name = "projet")
public class Projet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom_projet")
    private String nomProjet;

    @Column(name = "description_projet")
    private String descriptionProjet;

    @Column(name = "debut_projet")
    private LocalDate debutProjet;

    @Column(name = "fin_projet")
    private LocalDate finProjet;

    @Column(name = "jours_vendus_projet")
    private Float joursVendusProjet;

    @OneToMany(mappedBy = "projetMere")
    @JsonIgnore
    private Set<Tache> tacheEnfants = new HashSet<>();


    @ManyToMany
    @JoinTable(name = "projet_membres_projet",
               joinColumns = @JoinColumn(name="projets_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="membres_projets_id", referencedColumnName="id"))
    private Set<User> membresProjets = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "projet_chef_du_projet",
               joinColumns = @JoinColumn(name="projets_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="chef_du_projets_id", referencedColumnName="id"))
    private Set<User> chefDuProjets = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public Projet nomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
        return this;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getDescriptionProjet() {
        return descriptionProjet;
    }

    public Projet descriptionProjet(String descriptionProjet) {
        this.descriptionProjet = descriptionProjet;
        return this;
    }

    public void setDescriptionProjet(String descriptionProjet) {
        this.descriptionProjet = descriptionProjet;
    }

    public LocalDate getDebutProjet() {
        return debutProjet;
    }

    public Projet debutProjet(LocalDate debutProjet) {
        this.debutProjet = debutProjet;
        return this;
    }

    public void setDebutProjet(LocalDate debutProjet) {
        this.debutProjet = debutProjet;
    }

    public LocalDate getFinProjet() {
        return finProjet;
    }

    public Projet finProjet(LocalDate finProjet) {
        this.finProjet = finProjet;
        return this;
    }

    public void setFinProjet(LocalDate finProjet) {
        this.finProjet = finProjet;
    }

    public Float getJoursVendusProjet() {
        return joursVendusProjet;
    }

    public Projet joursVendusProjet(Float joursVendusProjet) {
        this.joursVendusProjet = joursVendusProjet;
        return this;
    }

    public void setJoursVendusProjet(Float joursVendusProjet) {
        this.joursVendusProjet = joursVendusProjet;
    }

    public Set<Tache> getTacheEnfants() {
        return tacheEnfants;
    }

    public Projet tacheEnfants(Set<Tache> taches) {
        this.tacheEnfants = taches;
        return this;
    }

    public Projet addTacheEnfant(Tache tache) {
        this.tacheEnfants.add(tache);
        tache.setProjetMere(this);
        return this;
    }

    public Projet removeTacheEnfant(Tache tache) {
        this.tacheEnfants.remove(tache);
        tache.setProjetMere(null);
        return this;
    }

    public void setTacheEnfants(Set<Tache> taches) {
        this.tacheEnfants = taches;
    }

    public Set<User> getMembresProjets() {
        return membresProjets;
    }

    public Projet membresProjets(Set<User> users) {
        this.membresProjets = users;
        return this;
    }

    public Projet addMembresProjet(User user) {
        this.membresProjets.add(user);
        return this;
    }

    public Projet removeMembresProjet(User user) {
        this.membresProjets.remove(user);
        return this;
    }

    public void setMembresProjets(Set<User> users) {
        this.membresProjets = users;
    }

    public Set<User> getChefDuProjets() {
        return chefDuProjets;
    }

    public Projet chefDuProjets(Set<User> users) {
        this.chefDuProjets = users;
        return this;
    }

    public Projet addChefDuProjet(User user) {
        this.chefDuProjets.add(user);
        return this;
    }

    public Projet removeChefDuProjet(User user) {
        this.chefDuProjets.remove(user);
        return this;
    }

    public void setChefDuProjets(Set<User> users) {
        this.chefDuProjets = users;
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
        Projet projet = (Projet) o;
        if (projet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Projet{" +
            "id=" + getId() +
            ", nomProjet='" + getNomProjet() + "'" +
            ", descriptionProjet='" + getDescriptionProjet() + "'" +
            ", debutProjet='" + getDebutProjet() + "'" +
            ", finProjet='" + getFinProjet() + "'" +
            ", joursVendusProjet='" + getJoursVendusProjet() + "'" +
            "}";
    }
}
