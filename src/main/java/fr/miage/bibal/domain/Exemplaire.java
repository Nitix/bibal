package fr.miage.bibal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
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

    @ManyToOne
    private Oeuvre oeuvre;

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

    public Oeuvre getOeuvre() {
        return oeuvre;
    }

    public Exemplaire oeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
        return this;
    }

    public void setOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
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
