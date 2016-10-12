package fr.miage.bibal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Exemplaire.
 */
@Entity
@Table(name = "exemplaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Exemplaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "etat", nullable = false)
    private String etat;

    @OneToMany(mappedBy = "exemplaire")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Oeuvre> oeuvres = new HashSet<>();

    @ManyToOne
    private Emprunt emprunt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return etat;
    }

    public Exemplaire etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Set<Oeuvre> getOeuvres() {
        return oeuvres;
    }

    public Exemplaire oeuvres(Set<Oeuvre> oeuvres) {
        this.oeuvres = oeuvres;
        return this;
    }

    public Exemplaire addOeuvre(Oeuvre oeuvre) {
        oeuvres.add(oeuvre);
        oeuvre.setExemplaire(this);
        return this;
    }

    public Exemplaire removeOeuvre(Oeuvre oeuvre) {
        oeuvres.remove(oeuvre);
        oeuvre.setExemplaire(null);
        return this;
    }

    public void setOeuvres(Set<Oeuvre> oeuvres) {
        this.oeuvres = oeuvres;
    }

    public Emprunt getEmprunt() {
        return emprunt;
    }

    public Exemplaire emprunt(Emprunt emprunt) {
        this.emprunt = emprunt;
        return this;
    }

    public void setEmprunt(Emprunt emprunt) {
        this.emprunt = emprunt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Exemplaire exemplaire = (Exemplaire) o;
        if(exemplaire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, exemplaire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Exemplaire{" +
            "id=" + id +
            ", etat='" + etat + "'" +
            '}';
    }
}
