package fr.miage.bibal.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.miage.bibal.domain.Usager;

import fr.miage.bibal.repository.UsagerRepository;
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
 * REST controller for managing Usager.
 */
@RestController
@RequestMapping("/api")
public class UsagerResource {

    private final Logger log = LoggerFactory.getLogger(UsagerResource.class);
        
    @Inject
    private UsagerRepository usagerRepository;

    /**
     * POST  /usagers : Create a new usager.
     *
     * @param usager the usager to create
     * @return the ResponseEntity with status 201 (Created) and with body the new usager, or with status 400 (Bad Request) if the usager has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/usagers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Usager> createUsager(@Valid @RequestBody Usager usager) throws URISyntaxException {
        log.debug("REST request to save Usager : {}", usager);
        if (usager.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("usager", "idexists", "A new usager cannot already have an ID")).body(null);
        }
        Usager result = usagerRepository.save(usager);
        return ResponseEntity.created(new URI("/api/usagers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("usager", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /usagers : Updates an existing usager.
     *
     * @param usager the usager to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated usager,
     * or with status 400 (Bad Request) if the usager is not valid,
     * or with status 500 (Internal Server Error) if the usager couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/usagers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Usager> updateUsager(@Valid @RequestBody Usager usager) throws URISyntaxException {
        log.debug("REST request to update Usager : {}", usager);
        if (usager.getId() == null) {
            return createUsager(usager);
        }
        Usager result = usagerRepository.save(usager);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("usager", usager.getId().toString()))
            .body(result);
    }

    /**
     * GET  /usagers : get all the usagers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of usagers in body
     */
    @RequestMapping(value = "/usagers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Usager> getAllUsagers() {
        log.debug("REST request to get all Usagers");
        List<Usager> usagers = usagerRepository.findAll();
        return usagers;
    }

    /**
     * GET  /usagers/:id : get the "id" usager.
     *
     * @param id the id of the usager to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the usager, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/usagers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Usager> getUsager(@PathVariable Long id) {
        log.debug("REST request to get Usager : {}", id);
        Usager usager = usagerRepository.findOne(id);
        return Optional.ofNullable(usager)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /usagers/:id : delete the "id" usager.
     *
     * @param id the id of the usager to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/usagers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUsager(@PathVariable Long id) {
        log.debug("REST request to delete Usager : {}", id);
        usagerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("usager", id.toString())).build();
    }

}
