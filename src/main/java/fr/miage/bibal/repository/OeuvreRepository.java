package fr.miage.bibal.repository;

import fr.miage.bibal.domain.Oeuvre;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Oeuvre entity.
 */
@SuppressWarnings("unused")
public interface OeuvreRepository extends JpaRepository<Oeuvre,Long> {

}
