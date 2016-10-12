package fr.miage.bibal.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Oeuvre.
 */
@Entity
@Table(name = "oeuvre")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Oeuvre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "titre", nullable = false)
    private String titre;

    @NotNull
    @Column(name = "editeur", nullable = false)
    private String editeur;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "data_publication")
    private ZonedDateTime dataPublication;

    @Column(name = "date_edition")
    private ZonedDateTime dateEdition;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "parution")
    private ZonedDateTime parution;

    @Column(name = "periodicite")
    private Integer periodicite;

    @NotNull
    @Column(name = "est_livre", nullable = false)
    private Boolean estLivre;

    @ManyToOne
    @NotNull
    private Auteur auteur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public Oeuvre titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getEditeur() {
        return editeur;
    }

    public Oeuvre editeur(String editeur) {
        this.editeur = editeur;
        return this;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public String getDescription() {
        return description;
    }

    public Oeuvre description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDataPublication() {
        return dataPublication;
    }

    public Oeuvre dataPublication(ZonedDateTime dataPublication) {
        this.dataPublication = dataPublication;
        return this;
    }

    public void setDataPublication(ZonedDateTime dataPublication) {
        this.dataPublication = dataPublication;
    }

    public ZonedDateTime getDateEdition() {
        return dateEdition;
    }

    public Oeuvre dateEdition(ZonedDateTime dateEdition) {
        this.dateEdition = dateEdition;
        return this;
    }

    public void setDateEdition(ZonedDateTime dateEdition) {
        this.dateEdition = dateEdition;
    }

    public Integer getNumero() {
        return numero;
    }

    public Oeuvre numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public ZonedDateTime getParution() {
        return parution;
    }

    public Oeuvre parution(ZonedDateTime parution) {
        this.parution = parution;
        return this;
    }

    public void setParution(ZonedDateTime parution) {
        this.parution = parution;
    }

    public Integer getPeriodicite() {
        return periodicite;
    }

    public Oeuvre periodicite(Integer periodicite) {
        this.periodicite = periodicite;
        return this;
    }

    public void setPeriodicite(Integer periodicite) {
        this.periodicite = periodicite;
    }

    public Boolean isEstLivre() {
        return estLivre;
    }

    public Oeuvre estLivre(Boolean estLivre) {
        this.estLivre = estLivre;
        return this;
    }

    public void setEstLivre(Boolean estLivre) {
        this.estLivre = estLivre;
    }

    public Auteur getAuteur() {
        return auteur;
    }

    public Oeuvre auteur(Auteur auteur) {
        this.auteur = auteur;
        return this;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Oeuvre oeuvre = (Oeuvre) o;
        if(oeuvre.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, oeuvre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Oeuvre{" +
            "id=" + id +
            ", titre='" + titre + "'" +
            ", editeur='" + editeur + "'" +
            ", description='" + description + "'" +
            ", dataPublication='" + dataPublication + "'" +
            ", dateEdition='" + dateEdition + "'" +
            ", numero='" + numero + "'" +
            ", parution='" + parution + "'" +
            ", periodicite='" + periodicite + "'" +
            ", estLivre='" + estLivre + "'" +
            '}';
    }
}
