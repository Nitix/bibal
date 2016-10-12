package fr.miage.bibal.repository;

import fr.miage.bibal.domain.Exemplaire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Exemplaire entity.
 */
@SuppressWarnings("unused")
public interface ExemplaireRepository extends JpaRepository<Exemplaire,Long> {

}
