package fr.miage.bibal.repository;

import fr.miage.bibal.domain.Exemplaire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Exemplaire entity.
 */
public interface ExemplaireRepository extends JpaRepository<Exemplaire,Long> {

    @Query(value = "select distinct exemplaire from Emprunt emprunt RIGHT JOIN emprunt.exemplaire exemplaire WHERE emprunt.dateRetour < NOW() OR emprunt.dateEmprunt = null")
    List<Exemplaire> findFree();
}
