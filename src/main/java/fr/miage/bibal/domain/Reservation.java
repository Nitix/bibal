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
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_reservation", nullable = false)
    private ZonedDateTime dateReservation;

    @OneToMany(mappedBy = "reservation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Usager> usagers = new HashSet<>();

    @OneToMany(mappedBy = "reservation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Oeuvre> oeuvres = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateReservation() {
        return dateReservation;
    }

    public Reservation dateReservation(ZonedDateTime dateReservation) {
        this.dateReservation = dateReservation;
        return this;
    }

    public void setDateReservation(ZonedDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Set<Usager> getUsagers() {
        return usagers;
    }

    public Reservation usagers(Set<Usager> usagers) {
        this.usagers = usagers;
        return this;
    }

    public Reservation addUsager(Usager usager) {
        usagers.add(usager);
        usager.setReservation(this);
        return this;
    }

    public Reservation removeUsager(Usager usager) {
        usagers.remove(usager);
        usager.setReservation(null);
        return this;
    }

    public void setUsagers(Set<Usager> usagers) {
        this.usagers = usagers;
    }

    public Set<Oeuvre> getOeuvres() {
        return oeuvres;
    }

    public Reservation oeuvres(Set<Oeuvre> oeuvres) {
        this.oeuvres = oeuvres;
        return this;
    }

    public Reservation addOeuvre(Oeuvre oeuvre) {
        oeuvres.add(oeuvre);
        oeuvre.setReservation(this);
        return this;
    }

    public Reservation removeOeuvre(Oeuvre oeuvre) {
        oeuvres.remove(oeuvre);
        oeuvre.setReservation(null);
        return this;
    }

    public void setOeuvres(Set<Oeuvre> oeuvres) {
        this.oeuvres = oeuvres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation reservation = (Reservation) o;
        if(reservation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reservation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + id +
            ", dateReservation='" + dateReservation + "'" +
            '}';
    }
}
