package fr.miage.bibal.repository;

import fr.miage.bibal.domain.Emprunt;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Emprunt entity.
 */
@SuppressWarnings("unused")
public interface EmpruntRepository extends JpaRepository<Emprunt,Long> {

}
