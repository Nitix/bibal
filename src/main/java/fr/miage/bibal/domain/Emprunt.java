package fr.miage.bibal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Emprunt.
 */
@Entity
@Table(name = "emprunt")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Emprunt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_emprunt", nullable = false)
    private ZonedDateTime dateEmprunt;

    @Column(name = "date_retour")
    private ZonedDateTime dateRetour;

    @OneToMany(mappedBy = "emprunt")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Usager> usagers = new HashSet<>();

    @OneToMany(mappedBy = "emprunt")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Exemplaire> exemplaires = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateEmprunt() {
        return dateEmprunt;
    }

    public Emprunt dateEmprunt(ZonedDateTime dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
        return this;
    }

    public void setDateEmprunt(ZonedDateTime dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public ZonedDateTime getDateRetour() {
        return dateRetour;
    }

    public Emprunt dateRetour(ZonedDateTime dateRetour) {
        this.dateRetour = dateRetour;
        return this;
    }

    public void setDateRetour(ZonedDateTime dateRetour) {
        this.dateRetour = dateRetour;
    }

    public Set<Usager> getUsagers() {
        return usagers;
    }

    public Emprunt usagers(Set<Usager> usagers) {
        this.usagers = usagers;
        return this;
    }

    public Emprunt addUsager(Usager usager) {
        usagers.add(usager);
        usager.setEmprunt(this);
        return this;
    }

    public Emprunt removeUsager(Usager usager) {
        usagers.remove(usager);
        usager.setEmprunt(null);
        return this;
    }

    public void setUsagers(Set<Usager> usagers) {
        this.usagers = usagers;
    }

    public Set<Exemplaire> getExemplaires() {
        return exemplaires;
    }

    public Emprunt exemplaires(Set<Exemplaire> exemplaires) {
        this.exemplaires = exemplaires;
        return this;
    }

    public Emprunt addExemplaire(Exemplaire exemplaire) {
        exemplaires.add(exemplaire);
        exemplaire.setEmprunt(this);
        return this;
    }

    public Emprunt removeExemplaire(Exemplaire exemplaire) {
        exemplaires.remove(exemplaire);
        exemplaire.setEmprunt(null);
        return this;
    }

    public void setExemplaires(Set<Exemplaire> exemplaires) {
        this.exemplaires = exemplaires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Emprunt emprunt = (Emprunt) o;
        if(emprunt.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, emprunt.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Emprunt{" +
            "id=" + id +
            ", dateEmprunt='" + dateEmprunt + "'" +
            ", dateRetour='" + dateRetour + "'" +
            '}';
    }
}
