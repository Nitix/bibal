package fr.miage.bibal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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

    @ManyToOne
    private Usager usager;

    @ManyToOne
    private Exemplaire exemplaire;

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

    public Usager getUsager() {
        return usager;
    }

    public Emprunt usager(Usager usager) {
        this.usager = usager;
        return this;
    }

    public void setUsager(Usager usager) {
        this.usager = usager;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public Emprunt exemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
        return this;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
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
