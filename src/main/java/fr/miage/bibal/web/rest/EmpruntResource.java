package fr.miage.bibal.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.miage.bibal.domain.Emprunt;

import fr.miage.bibal.repository.EmpruntRepository;
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

/**
 * REST controller for managing Emprunt.
 */
@RestController
@RequestMapping("/api")
public class EmpruntResource {

    private final Logger log = LoggerFactory.getLogger(EmpruntResource.class);
        
    @Inject
    private EmpruntRepository empruntRepository;

    /**
     * POST  /emprunts : Create a new emprunt.
     *
     * @param emprunt the emprunt to create
     * @return the ResponseEntity with status 201 (Created) and with body the new emprunt, or with status 400 (Bad Request) if the emprunt has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/emprunts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Emprunt> createEmprunt(@Valid @RequestBody Emprunt emprunt) throws URISyntaxException {
        log.debug("REST request to save Emprunt : {}", emprunt);
        if (emprunt.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("emprunt", "idexists", "A new emprunt cannot already have an ID")).body(null);
        }
        Emprunt result = empruntRepository.save(emprunt);
        return ResponseEntity.created(new URI("/api/emprunts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("emprunt", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /emprunts : Updates an existing emprunt.
     *
     * @param emprunt the emprunt to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated emprunt,
     * or with status 400 (Bad Request) if the emprunt is not valid,
     * or with status 500 (Internal Server Error) if the emprunt couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/emprunts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Emprunt> updateEmprunt(@Valid @RequestBody Emprunt emprunt) throws URISyntaxException {
        log.debug("REST request to update Emprunt : {}", emprunt);
        if (emprunt.getId() == null) {
            return createEmprunt(emprunt);
        }
        Emprunt result = empruntRepository.save(emprunt);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("emprunt", emprunt.getId().toString()))
            .body(result);
    }

    /**
     * GET  /emprunts : get all the emprunts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of emprunts in body
     */
    @RequestMapping(value = "/emprunts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Emprunt> getAllEmprunts() {
        log.debug("REST request to get all Emprunts");
        List<Emprunt> emprunts = empruntRepository.findAll();
        return emprunts;
    }

    /**
     * GET  /emprunts/:id : get the "id" emprunt.
     *
     * @param id the id of the emprunt to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the emprunt, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/emprunts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Emprunt> getEmprunt(@PathVariable Long id) {
        log.debug("REST request to get Emprunt : {}", id);
        Emprunt emprunt = empruntRepository.findOne(id);
        return Optional.ofNullable(emprunt)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /emprunts/:id : delete the "id" emprunt.
     *
     * @param id the id of the emprunt to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/emprunts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmprunt(@PathVariable Long id) {
        log.debug("REST request to delete Emprunt : {}", id);
        empruntRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("emprunt", id.toString())).build();
    }

}
