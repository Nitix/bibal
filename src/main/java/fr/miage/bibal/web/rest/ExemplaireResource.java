package fr.miage.bibal.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.miage.bibal.domain.Exemplaire;

import fr.miage.bibal.repository.ExemplaireRepository;
import fr.miage.bibal.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * REST controller for managing Exemplaire.
 */
@RestController
@RequestMapping("/api")
public class ExemplaireResource {

    private final Logger log = LoggerFactory.getLogger(ExemplaireResource.class);

    @Inject
    private ExemplaireRepository exemplaireRepository;

    /**
     * POST  /exemplaires : Create a new exemplaire.
     *
     * @param exemplaire the exemplaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new exemplaire, or with status 400 (Bad Request) if the exemplaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/exemplaires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Exemplaire> createExemplaire(@Valid @RequestBody Exemplaire exemplaire) throws URISyntaxException {
        log.debug("REST request to save Exemplaire : {}", exemplaire);
        if (exemplaire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("exemplaire", "idexists", "A new exemplaire cannot already have an ID")).body(null);
        }
        Exemplaire result = exemplaireRepository.save(exemplaire);
        return ResponseEntity.created(new URI("/api/exemplaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("exemplaire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exemplaires : Updates an existing exemplaire.
     *
     * @param exemplaire the exemplaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated exemplaire,
     * or with status 400 (Bad Request) if the exemplaire is not valid,
     * or with status 500 (Internal Server Error) if the exemplaire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/exemplaires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Exemplaire> updateExemplaire(@Valid @RequestBody Exemplaire exemplaire) throws URISyntaxException {
        log.debug("REST request to update Exemplaire : {}", exemplaire);
        if (exemplaire.getId() == null) {
            return createExemplaire(exemplaire);
        }
        Exemplaire result = exemplaireRepository.save(exemplaire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("exemplaire", exemplaire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exemplaires : get all the exemplaires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of exemplaires in body
     */
    @RequestMapping(value = "/exemplaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Exemplaire> getAllExemplaires(@RequestParam(required=false) Boolean free) {
        if(free != null){
            return getAllFreeExemplaires();
        }
        log.debug("REST request to get all Exemplaires");
        List<Exemplaire> exemplaires = exemplaireRepository.findAll();
        return exemplaires;
    }

    public List<Exemplaire> getAllFreeExemplaires() {
        log.debug("REST request to get all Exemplaires");
        List<Exemplaire> exemplaires = exemplaireRepository.findFree();
        return exemplaires;
    }

    /**
     * GET  /exemplaires/:id : get the "id" exemplaire.
     *
     * @param id the id of the exemplaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the exemplaire, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/exemplaires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Exemplaire> getExemplaire(@PathVariable Long id) {
        log.debug("REST request to get Exemplaire : {}", id);
        Exemplaire exemplaire = exemplaireRepository.findOne(id);
        return Optional.ofNullable(exemplaire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /exemplaires/:id : delete the "id" exemplaire.
     *
     * @param id the id of the exemplaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/exemplaires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExemplaire(@PathVariable Long id) {
        log.debug("REST request to delete Exemplaire : {}", id);
        exemplaireRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("exemplaire", id.toString())).build();
    }

}
