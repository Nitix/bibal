package fr.miage.bibal.repository;

import fr.miage.bibal.domain.Usager;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Usager entity.
 */
@SuppressWarnings("unused")
public interface UsagerRepository extends JpaRepository<Usager,Long> {

}
