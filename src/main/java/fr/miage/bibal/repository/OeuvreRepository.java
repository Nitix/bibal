package fr.miage.bibal.repository;

import fr.miage.bibal.domain.Oeuvre;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Oeuvre entity.
 */
public interface OeuvreRepository extends JpaRepository<Oeuvre,Long> {

    @Query("SELECT DISTINCT oeuvre from Exemplaire exemplaire INNER JOIN exemplaire.oeuvre oeuvre")
    List<Oeuvre> findWithExemplaire();

}
